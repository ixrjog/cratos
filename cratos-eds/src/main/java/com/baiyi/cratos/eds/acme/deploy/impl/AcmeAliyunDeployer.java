package com.baiyi.cratos.eds.acme.deploy.impl;

import com.baiyi.cratos.common.util.PasswordGenerator;
import com.baiyi.cratos.common.util.TimeUtils;
import com.baiyi.cratos.domain.generator.AcmeCertificate;
import com.baiyi.cratos.domain.generator.AcmeDomain;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.util.StringFormatter;
import com.baiyi.cratos.eds.acme.deploy.BaseAcmeDeployer;
import com.baiyi.cratos.eds.aliyun.repo.AliyunCertRepo;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.service.acme.AcmeDomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/2/28 10:10
 * &#064;Version 1.0
 */
@Slf4j
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.ALIYUN, assetTypeOf = EdsAssetTypeEnum.ALIYUN_DOMAIN)
public class AcmeAliyunDeployer extends BaseAcmeDeployer<EdsConfigs.Aliyun> {

    private final AliyunCertRepo aliyunCertRepo;
    private final AcmeDomainService acmeDomainService;

    public AcmeAliyunDeployer(EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder,
                              AliyunCertRepo aliyunCertRepo, AcmeDomainService acmeDomainService) {
        super(edsInstanceProviderHolderBuilder);
        this.aliyunCertRepo = aliyunCertRepo;
        this.acmeDomainService = acmeDomainService;
    }

    @Override
    public void deployCert(EdsInstance edsInstance, AcmeCertificate acmeCertificate) {
        EdsConfigs.Aliyun aliyun = getEdsConfig(edsInstance);
        AcmeDomain acmeDomain = acmeDomainService.getById(acmeCertificate.getDomainId());
        if (!findDomain(aliyun, acmeDomain.getDomain())) {
            return;
        }
        String cert = acmeCertificate.getCertificate() + "\n" + acmeCertificate.getCertificateChain();
        String certName = StringFormatter.arrayFormat(
                CERT_NAME_TPL, PasswordGenerator.generateTicketNo(),
                TimeUtils.parse(acmeCertificate.getNotAfter(), "yyyy-MM-dd")
        );
        try {
            aliyunCertRepo.uploadUserCertificate(aliyun, certName, cert, acmeCertificate.getPrivateKey());
            log.info(
                    "Upload certificate {} {} to {}", certName, acmeCertificate.getDomains(),
                    edsInstance.getInstanceName()
            );
        } catch (Exception ignored) {
        }
    }

}
