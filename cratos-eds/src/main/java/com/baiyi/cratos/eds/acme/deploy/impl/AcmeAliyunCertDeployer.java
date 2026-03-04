package com.baiyi.cratos.eds.acme.deploy.impl;

import com.aliyun.cas20200407.models.ListCertificatesResponseBody;
import com.aliyun.cas20200407.models.ListCloudResourcesResponseBody;
import com.baiyi.cratos.domain.generator.AcmeCertificate;
import com.baiyi.cratos.domain.generator.AcmeCertificateDeployment;
import com.baiyi.cratos.domain.generator.AcmeDomain;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.eds.acme.deploy.BaseAcmeDeployer;
import com.baiyi.cratos.eds.aliyun.repo.AliyunCertRepo;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.service.acme.AcmeCertificateDeploymentService;
import com.baiyi.cratos.service.acme.AcmeDomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/2/28 10:10
 * &#064;Version 1.0
 */
@Slf4j
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.ALIYUN, assetTypeOf = EdsAssetTypeEnum.ALIYUN_CERT)
public class AcmeAliyunCertDeployer extends BaseAcmeDeployer<EdsConfigs.Aliyun> {

    private final AliyunCertRepo aliyunCertRepo;
    private final AcmeDomainService acmeDomainService;
    private final AcmeCertificateDeploymentService acmeCertificateDeploymentService;
    private final EdsInstanceService edsInstanceService;

    public AcmeAliyunCertDeployer(EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder,
                                  AliyunCertRepo aliyunCertRepo, AcmeDomainService acmeDomainService,
                                  AcmeCertificateDeploymentService acmeCertificateDeploymentService,
                                  EdsInstanceService edsInstanceService) {
        super(edsInstanceProviderHolderBuilder);
        this.aliyunCertRepo = aliyunCertRepo;
        this.acmeDomainService = acmeDomainService;
        this.acmeCertificateDeploymentService = acmeCertificateDeploymentService;
        this.edsInstanceService = edsInstanceService;
    }

    @Override
    public void deployCert(EdsInstance edsInstance, AcmeCertificate acmeCertificate) {
        EdsConfigs.Aliyun aliyun = getEdsConfig(edsInstance);
        AcmeDomain acmeDomain = acmeDomainService.getById(acmeCertificate.getDomainId());
        if (!findDomain(aliyun, acmeDomain.getDomain())) {
            return;
        }
        String cert = mergeCertificatesAndCertificateChains(acmeCertificate);
        String certName = generateCertName(acmeCertificate);
        try {
            log.info(
                    "Upload certificate {} {} to {}", certName, acmeCertificate.getDomains(),
                    edsInstance.getInstanceName()
            );
            Long certId = aliyunCertRepo.uploadUserCertificate(aliyun, certName, cert, acmeCertificate.getPrivateKey());
            // 创建 deployment
            AcmeCertificateDeployment deployment = AcmeCertificateDeployment.builder()
                    .edsCertificateId(String.valueOf(certId))
                    .edsInstanceId(edsInstance.getId())
                    .edsInstanceName(edsInstance.getInstanceName())
                    .edsCertificateName(certName)
                    .certificateId(acmeCertificate.getId())
                    .domain(acmeDomain.getDomain())
                    .domains(acmeCertificate.getDomains())
                    .notAfter(acmeCertificate.getNotAfter())
                    .notBefore(acmeCertificate.getNotBefore())
                    .valid(true)
                    .build();
            acmeCertificateDeploymentService.add(deployment);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    public List<ListCloudResourcesResponseBody.ListCloudResourcesResponseBodyData> listCloudResourcesByCertName(int acmeCertificateDeploymentId,
                                                                                        String certificateName) {
        AcmeCertificateDeployment deployment = acmeCertificateDeploymentService.getById(acmeCertificateDeploymentId);
        EdsInstance edsInstance = edsInstanceService.getById(deployment.getEdsInstanceId());
        EdsConfigs.Aliyun aliyun = getEdsConfig(edsInstance);
        try {
            ListCertificatesResponseBody.ListCertificatesResponseBodyCertificateList cert = aliyunCertRepo.queryCertificateByName(
                    aliyun, certificateName);
            if (cert != null) {
                return Optional.ofNullable(
                                aliyunCertRepo.listCloudResources(aliyun, Long.valueOf(cert.getCertificateId())))
                        .orElse(List.of());
            }
        } catch (Exception ex) {
          log.error(ex.getMessage());
        }
        return List.of();
    }

}
