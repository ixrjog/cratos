package com.baiyi.cratos.eds.business.wrapper.impl.domain;

import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.eds.business.wrapper.impl.domain.base.BaseDomainAssetToBusinessWrapper;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.domain.gandi.model.GandiDomain;
import com.baiyi.cratos.service.BusinessAssetBoundService;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/6/4 下午6:06
 * @Version 1.0
 */
@Component
@BusinessType(type = BusinessTypeEnum.DOMAIN)
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.GANDI, assetTypeOf = EdsAssetTypeEnum.GANDI_DOMAIN)
public class GandiDomainAssetToBusinessWrapper extends BaseDomainAssetToBusinessWrapper<GandiDomain.Domain> {

    public GandiDomainAssetToBusinessWrapper(BusinessAssetBoundService businessAssetBoundService) {
        super(businessAssetBoundService);
    }

}