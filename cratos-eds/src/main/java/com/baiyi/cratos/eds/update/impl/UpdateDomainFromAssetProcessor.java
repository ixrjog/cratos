package com.baiyi.cratos.eds.update.impl;

import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.BusinessAssetBound;
import com.baiyi.cratos.domain.generator.Domain;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.update.BaseUpdateBusinessFromAssetProcessor;
import com.baiyi.cratos.service.BusinessAssetBoundService;
import com.baiyi.cratos.service.DomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/6/7 下午1:38
 * &#064;Version 1.0
 */
@Slf4j
@Component
@BusinessType(type = BusinessTypeEnum.DOMAIN)
public class UpdateDomainFromAssetProcessor extends BaseUpdateBusinessFromAssetProcessor<Domain> {

    private final DomainService domainService;

    public UpdateDomainFromAssetProcessor(BusinessAssetBoundService businessAssetBoundService,
                                          DomainService domainService) {
        super(businessAssetBoundService);
        this.domainService = domainService;
    }

    @Override
    protected Domain getBusiness(BusinessAssetBound businessAssetBound) {
        return domainService.getById(businessAssetBound.getBusinessId());
    }

    @Override
    protected void updateBusiness(EdsAsset asset, Domain business) {
        business.setExpiry(asset.getExpiredTime());
        domainService.updateByPrimaryKey(business);
    }

}
