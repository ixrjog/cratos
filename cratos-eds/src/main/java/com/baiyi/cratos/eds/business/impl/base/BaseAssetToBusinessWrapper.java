package com.baiyi.cratos.eds.business.impl.base;

import com.baiyi.cratos.domain.generator.BusinessAssetBind;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.domain.view.eds.IToBusinessTarget;
import com.baiyi.cratos.eds.business.IAssetToBusinessWrapper;
import com.baiyi.cratos.eds.core.EdsInstanceProviderFactory;
import com.baiyi.cratos.service.BusinessAssetBindService;
import jakarta.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2024/3/12 10:49
 * @Version 1.0
 */
public abstract class BaseAssetToBusinessWrapper<B extends IToBusinessTarget,T> implements IAssetToBusinessWrapper<B> {

    @Resource
    protected BusinessAssetBindService businessAssetBindService;

    protected T getAssetModel(EdsAssetVO.Asset asset) {
        return EdsInstanceProviderFactory.produceModel(getInstanceType(), getAssetType(), asset);
    }

    @Override
    public void wrap(EdsAssetVO.Asset asset) {
        BusinessAssetBind businessAssetBind = getBusinessAssetBind(asset.getId());
        EdsAssetVO.ToBusiness toBusiness;
        if (businessAssetBind == null) {
            toBusiness = EdsAssetVO.ToBusiness.builder()
                    .businessType(getBusinessType())
                    .assetId(asset.getId())
                    .bind(false)
                    .build();
        } else {
            toBusiness = EdsAssetVO.ToBusiness.builder()
                    .businessType(getBusinessType())
                    .businessId(businessAssetBind.getBusinessId())
                    .assetId(asset.getId())
                    .bind(true)
                    .build();
        }
        asset.setToBusiness(toBusiness);
    }

    protected BusinessAssetBind getBusinessAssetBind(Integer assetId) {
        BusinessAssetBind query = BusinessAssetBind.builder()
                .businessType(getBusinessType())
                .assetId(assetId)
                .build();
        return businessAssetBindService.getByUniqueKey(query);
    }

}
