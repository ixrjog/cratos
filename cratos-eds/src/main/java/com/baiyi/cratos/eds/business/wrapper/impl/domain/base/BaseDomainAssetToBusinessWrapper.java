package com.baiyi.cratos.eds.business.wrapper.impl.domain.base;

import com.baiyi.cratos.domain.generator.Domain;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.eds.business.exception.AssetToBusinessException;
import com.baiyi.cratos.eds.business.wrapper.impl.BaseAssetToBusinessWrapper;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.service.BusinessAssetBoundService;

/**
 * @Author baiyi
 * @Date 2024/4/28 上午10:56
 * @Version 1.0
 */
public abstract class BaseDomainAssetToBusinessWrapper<B> extends BaseAssetToBusinessWrapper<Domain, B> {

    public BaseDomainAssetToBusinessWrapper(BusinessAssetBoundService businessAssetBoundService) {
        super(businessAssetBoundService);
    }

    protected String getDomainType() {
        EdsInstanceAssetType annotation = this.getClass().getAnnotation(EdsInstanceAssetType.class);
        if (annotation == null) {
            AssetToBusinessException.runtime("EdsInstanceAssetType annotation not found.");
        }
        return annotation.assetTypeOf().name();
    }

    @Override
    protected Domain toTarget(EdsAssetVO.Asset asset) {
        // AliyunDomain model = getAssetModel(asset);
        return Domain.builder()
                .expiry(asset.getExpiredTime())
                .registrationTime(asset.getCreatedTime())
                .domainType(getDomainType())
                .name(asset.getName())
                .valid(true)
                .build();
    }

}
