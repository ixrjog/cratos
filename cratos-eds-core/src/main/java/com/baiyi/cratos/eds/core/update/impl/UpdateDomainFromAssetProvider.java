package com.baiyi.cratos.eds.core.update.impl;

import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.BusinessAssetBind;
import com.baiyi.cratos.domain.generator.Domain;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.core.update.BaseUpdateBusinessFromAssetProvider;
import com.baiyi.cratos.service.BusinessAssetBindService;
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
public class UpdateDomainFromAssetProvider extends BaseUpdateBusinessFromAssetProvider<Domain> {

    private final DomainService domainService;

    public UpdateDomainFromAssetProvider(BusinessAssetBindService businessAssetBindService,
                                         DomainService domainService) {
        super(businessAssetBindService);
        this.domainService = domainService;
    }

    @Override
    protected Domain getBusiness(BusinessAssetBind businessAssetBind) {
        return domainService.getById(businessAssetBind.getBusinessId());
    }

    @Override
    protected void updateBusiness(EdsAsset asset, Domain business) {
        business.setExpiry(asset.getExpiredTime());
        domainService.updateByPrimaryKey(business);
    }

}
