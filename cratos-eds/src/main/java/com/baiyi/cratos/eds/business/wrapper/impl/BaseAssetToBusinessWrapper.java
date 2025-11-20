package com.baiyi.cratos.eds.business.wrapper.impl;

import com.baiyi.cratos.domain.generator.BusinessAssetBound;
import com.baiyi.cratos.domain.view.ToBusinessTarget;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.eds.business.wrapper.IAssetToBusinessWrapper;
import com.baiyi.cratos.eds.core.EdsInstanceProviderFactory;
import com.baiyi.cratos.service.BusinessAssetBoundService;
import lombok.RequiredArgsConstructor;

/**
 * @Author baiyi
 * @Date 2024/3/12 10:49
 * @Version 1.0
 */
@RequiredArgsConstructor
public abstract class BaseAssetToBusinessWrapper<B extends ToBusinessTarget, T> implements IAssetToBusinessWrapper<B> {

    protected final BusinessAssetBoundService businessAssetBoundService;

    protected T getAssetModel(EdsAssetVO.Asset asset) {
        return EdsInstanceProviderFactory.produceModel(getInstanceType(), getAssetType(), asset);
    }

    @Override
    public EdsAssetVO.AssetToBusiness<B> getAssetToBusiness(EdsAssetVO.Asset asset) {
        return EdsAssetVO.AssetToBusiness.<B>builder()
                .target(toTarget(asset))
                .toBusiness(getToBusiness(asset.getId()))
                .build();
    }

    protected abstract B toTarget(EdsAssetVO.Asset asset);

    @Override
    public void wrap(EdsAssetVO.Asset asset) {
        asset.setToBusiness(getToBusiness(asset.getId()));
    }

    protected EdsAssetVO.ToBusiness getToBusiness(int assetId) {
        BusinessAssetBound businessAssetBound = getBusinessAssetBound(assetId);
        return businessAssetBound == null ? EdsAssetVO.ToBusiness.builder()
                .businessType(getBusinessType())
                .assetId(assetId)
                .bind(false)
                .build() : EdsAssetVO.ToBusiness.builder()
                .businessType(getBusinessType())
                .businessId(businessAssetBound.getBusinessId())
                .assetId(assetId)
                .bind(true)
                .build();
    }

    protected BusinessAssetBound getBusinessAssetBound(Integer assetId) {
        BusinessAssetBound query = BusinessAssetBound.builder()
                .businessType(getBusinessType())
                .assetId(assetId)
                .build();
        return businessAssetBoundService.getByUniqueKey(query);
    }

}
