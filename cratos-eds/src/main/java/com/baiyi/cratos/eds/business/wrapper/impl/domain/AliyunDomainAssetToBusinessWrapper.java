package com.baiyi.cratos.eds.business.wrapper.impl.domain;

import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.eds.aliyun.model.AliyunDomain;
import com.baiyi.cratos.eds.business.wrapper.impl.domain.base.BaseDomainAssetToBusinessWrapper;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.service.BusinessAssetBoundService;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/4/28 上午10:45
 * @Version 1.0
 */
@Component
@BusinessType(type = BusinessTypeEnum.DOMAIN)
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.ALIYUN, assetTypeOf = EdsAssetTypeEnum.ALIYUN_DOMAIN)
public class AliyunDomainAssetToBusinessWrapper extends BaseDomainAssetToBusinessWrapper<AliyunDomain> {

    public AliyunDomainAssetToBusinessWrapper(BusinessAssetBoundService businessAssetBoundService) {
        super(businessAssetBoundService);
    }

    @Override
    protected String getDomainType() {
        return EdsAssetTypeEnum.ALIYUN_DOMAIN.name();
    }

}