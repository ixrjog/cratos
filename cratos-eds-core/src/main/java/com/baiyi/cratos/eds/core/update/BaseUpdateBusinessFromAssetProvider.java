package com.baiyi.cratos.eds.core.update;

import com.baiyi.cratos.domain.generator.BusinessAssetBind;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.service.BusinessAssetBindService;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/6/7 下午1:39
 * &#064;Version 1.0
 */
public abstract class BaseUpdateBusinessFromAssetProvider<B> implements IUpdateBusinessFromAssetProvider {

    public BaseUpdateBusinessFromAssetProvider(BusinessAssetBindService businessAssetBindService) {
        this.businessAssetBindService = businessAssetBindService;
    }

    private final BusinessAssetBindService businessAssetBindService;

    @Override
    public void update(EdsAsset asset, BusinessAssetBind businessAssetBind) {
        B business = getBusiness(businessAssetBind);
        if (business != null) {
            updateBusiness(asset, business);
        } else {
            try {
                businessAssetBindService.deleteById(businessAssetBind.getId());
            } catch (Exception ignored) {
            }
        }
    }

    abstract protected B getBusiness(BusinessAssetBind businessAssetBind);

    abstract protected void updateBusiness(EdsAsset asset, B business);

    @Override
    public void afterPropertiesSet() throws Exception {
        UpdateBusinessFromAssetProviderFactory.register(this);
    }

}
