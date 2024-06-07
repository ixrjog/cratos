package com.baiyi.cratos.eds.business.impl.base;

import com.baiyi.cratos.domain.generator.BusinessAssetBind;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.domain.view.ToBusinessTarget;
import com.baiyi.cratos.eds.business.IAssetToBusinessWrapper;
import com.baiyi.cratos.eds.core.EdsInstanceProviderFactory;
import com.baiyi.cratos.service.BusinessAssetBindService;
import jakarta.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2024/3/12 10:49
 * @Version 1.0
 */
public abstract class BaseAssetToBusinessWrapper<B extends ToBusinessTarget, T> implements IAssetToBusinessWrapper<B> {

    @Resource
    protected BusinessAssetBindService businessAssetBindService;

    protected T getAssetModel(EdsAssetVO.Asset asset) {
        return EdsInstanceProviderFactory.produceModel(getInstanceType(), getAssetType(), asset);
    }

    @Override
    public EdsAssetVO.AssetToBusiness<B> getAssetToBusiness(EdsAssetVO.Asset asset) {
        return EdsAssetVO.AssetToBusiness.<B>builder()
                .target(getTarget(asset))
                .toBusiness(getToBusiness(asset.getId()))
                .build();
    }

    protected abstract B getTarget(EdsAssetVO.Asset asset);

    @Override
    public void wrap(EdsAssetVO.Asset asset) {
        asset.setToBusiness(getToBusiness(asset.getId()));
    }

    protected EdsAssetVO.ToBusiness getToBusiness(int assetId) {
        BusinessAssetBind businessAssetBind = getBusinessAssetBind(assetId);
        return businessAssetBind == null ? EdsAssetVO.ToBusiness.builder()
                .businessType(getBusinessType())
                .assetId(assetId)
                .bind(false)
                .build() : EdsAssetVO.ToBusiness.builder()
                .businessType(getBusinessType())
                .businessId(businessAssetBind.getBusinessId())
                .assetId(assetId)
                .bind(true)
                .build();
    }

    protected BusinessAssetBind getBusinessAssetBind(Integer assetId) {
        BusinessAssetBind query = BusinessAssetBind.builder()
                .businessType(getBusinessType())
                .assetId(assetId)
                .build();
        return businessAssetBindService.getByUniqueKey(query);
    }

}
