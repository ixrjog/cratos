package com.baiyi.cratos.eds.core.update.impl;

import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.BusinessAssetBind;
import com.baiyi.cratos.domain.generator.Certificate;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.core.update.BaseUpdateBusinessFromAssetProcessor;
import com.baiyi.cratos.service.BusinessAssetBindService;
import com.baiyi.cratos.service.CertificateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/7/22 上午11:24
 * &#064;Version 1.0
 */
@Slf4j
@Component
@BusinessType(type = BusinessTypeEnum.CERTIFICATE)
public class UpdateCertFromAssetProcessor extends BaseUpdateBusinessFromAssetProcessor<Certificate> {

    private final CertificateService certificateService;

    public UpdateCertFromAssetProcessor(BusinessAssetBindService businessAssetBindService,
                                        CertificateService certificateService) {
        super(businessAssetBindService);
        this.certificateService = certificateService;
    }

    @Override
    protected Certificate getBusiness(BusinessAssetBind businessAssetBind) {
        return certificateService.getById(businessAssetBind.getBusinessId());
    }

    @Override
    protected void updateBusiness(EdsAsset asset, Certificate business) {
        business.setExpiredTime(asset.getExpiredTime());
        business.setNotAfter(asset.getExpiredTime());
        certificateService.updateByPrimaryKey(business);
    }

}