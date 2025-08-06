package com.baiyi.cratos.eds.business.wrapper.impl.domain;

import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.eds.business.wrapper.impl.domain.base.BaseDomainAssetToBusinessWrapper;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.domain.model.GandiDomain;
import com.baiyi.cratos.service.BusinessAssetBindService;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/6/4 下午6:06
 * &#064;Version 1.0
 */
@Component
@BusinessType(type = BusinessTypeEnum.DOMAIN)
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.GANDI, assetTypeOf = EdsAssetTypeEnum.GANDI_DOMAIN)
public class GandiDomainAssetToBusinessWrapper extends BaseDomainAssetToBusinessWrapper<GandiDomain.Domain> {

    public GandiDomainAssetToBusinessWrapper(BusinessAssetBindService businessAssetBindService) {
        super(businessAssetBindService);
    }

    @Override
    protected String getDomainType() {
        return EdsAssetTypeEnum.GANDI_DOMAIN.name();
    }

}