package com.baiyi.cratos.eds.acme.deploy.impl;

import com.baiyi.cratos.domain.generator.AcmeCertificate;
import com.baiyi.cratos.domain.generator.AcmeDomain;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.eds.acme.deploy.BaseAcmeDeployer;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.huaweicloud.cloud.repo.HwcElbRepo;
import com.baiyi.cratos.service.acme.AcmeDomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/3/2 11:34
 * &#064;Version 1.0
 */
@Slf4j
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.HUAWEICLOUD, assetTypeOf = EdsAssetTypeEnum.HUAWEICLOUD_ELB_CERT)
public class AcmeHuaweiCloudElbCertDeployer extends BaseAcmeDeployer<EdsConfigs.Hwc> {

    private final AcmeDomainService acmeDomainService;

    public AcmeHuaweiCloudElbCertDeployer(EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder,
                                          AcmeDomainService acmeDomainService) {
        super(edsInstanceProviderHolderBuilder);
        this.acmeDomainService = acmeDomainService;
    }

    @Override
    public void deployCert(EdsInstance edsInstance, AcmeCertificate acmeCertificate) {
        EdsConfigs.Hwc hwc = getEdsConfig(edsInstance);
        AcmeDomain acmeDomain = acmeDomainService.getById(acmeCertificate.getDomainId());
        if (!findDomain(hwc, acmeDomain.getDomain())) {
            return;
        }
        String cert = mergeCertificatesAndCertificateChains(acmeCertificate);
        String certName = generateCertName(acmeCertificate);

        hwc.getRegionIds()
                .forEach(region -> {
                    try {
                        HwcElbRepo.createCertificate(
                                region, hwc, certName, acmeCertificate.getDomains(), cert,
                                acmeCertificate.getPrivateKey()
                        );
                        log.info(
                                "Upload certificate {} {} to {}", certName, acmeCertificate.getDomains(),
                                edsInstance.getInstanceName()
                        );
                    } catch (Exception ex) {
                        log.error(ex.getMessage());
                    }
                });
    }

}
