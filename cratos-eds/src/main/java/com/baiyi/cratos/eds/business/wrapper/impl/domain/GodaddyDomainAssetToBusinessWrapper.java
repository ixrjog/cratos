package com.baiyi.cratos.eds.business.wrapper.impl.domain;

import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.eds.business.wrapper.impl.domain.base.BaseDomainAssetToBusinessWrapper;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.domain.model.GodaddyDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/6/5 下午3:50
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.DOMAIN)
@EdsInstanceAssetType(instanceType = EdsInstanceTypeEnum.GODADDY, assetType = EdsAssetTypeEnum.GODADDY_DOMAIN)
public class GodaddyDomainAssetToBusinessWrapper extends BaseDomainAssetToBusinessWrapper<GodaddyDomain.Domain> {

    @Override
    protected String getDomainType() {
        return EdsAssetTypeEnum.GODADDY_DOMAIN.name();
    }

}