package com.baiyi.cratos.eds.business.wrapper.impl.domain;

import com.amazonaws.services.route53domains.model.DomainSummary;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.eds.business.wrapper.impl.domain.base.BaseDomainAssetToBusinessWrapper;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.service.BusinessAssetBindService;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/4/28 上午10:53
 * @Version 1.0
 */
@Component
@BusinessType(type = BusinessTypeEnum.DOMAIN)
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.AWS, assetTypeOf = EdsAssetTypeEnum.AWS_DOMAIN)
public class AwsDomainAssetToBusinessWrapper extends BaseDomainAssetToBusinessWrapper<DomainSummary> {

    public AwsDomainAssetToBusinessWrapper(BusinessAssetBindService businessAssetBindService) {
        super(businessAssetBindService);
    }

    @Override
    protected String getDomainType() {
        return EdsAssetTypeEnum.AWS_DOMAIN.name();
    }

}