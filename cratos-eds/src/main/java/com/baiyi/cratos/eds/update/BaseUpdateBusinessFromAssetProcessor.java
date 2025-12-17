package com.baiyi.cratos.eds.update;

import com.baiyi.cratos.domain.generator.BusinessAssetBound;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.service.BusinessAssetBoundService;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/6/7 下午1:39
 * &#064;Version 1.0
 */
public abstract class BaseUpdateBusinessFromAssetProcessor<B> implements IUpdateBusinessFromAssetProcessor {

    public BaseUpdateBusinessFromAssetProcessor(BusinessAssetBoundService businessAssetBoundService) {
        this.businessAssetBoundService = businessAssetBoundService;
    }

    private final BusinessAssetBoundService businessAssetBoundService;

    @Override
    public void update(EdsAsset asset, BusinessAssetBound businessAssetBound) {
        B business = getBusiness(businessAssetBound);
        if (business != null) {
            updateBusiness(asset, business);
        } else {
            try {
                businessAssetBoundService.deleteById(businessAssetBound.getId());
            } catch (Exception ignored) {
            }
        }
    }

    abstract protected B getBusiness(BusinessAssetBound businessAssetBound);

    abstract protected void updateBusiness(EdsAsset asset, B business);

    @Override
    public void afterPropertiesSet() throws Exception {
        UpdateBusinessFromAssetProcessorFactory.register(this);
    }

}
