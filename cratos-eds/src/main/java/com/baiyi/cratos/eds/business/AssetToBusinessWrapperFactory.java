package com.baiyi.cratos.eds.business;

import com.baiyi.cratos.domain.view.eds.IToBusinessTarget;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2024/3/12 10:13
 * @Version 1.0
 */
@Slf4j
public class AssetToBusinessWrapperFactory {

    private AssetToBusinessWrapperFactory() {
    }

    private static final Map<String, IAssetToBusinessWrapper<?>> CONTEXT = new ConcurrentHashMap<>();

    public static <B extends IToBusinessTarget> void register(IAssetToBusinessWrapper<B> providerBean) {
        log.info("AssetToBusinessProviderFactory Registered: assetType={}", providerBean.getAssetType());
        CONTEXT.put(providerBean.getAssetType(), providerBean);
    }

    @SuppressWarnings("unchecked")
    public static <B extends IToBusinessTarget> IAssetToBusinessWrapper<B> getProvider(String assetType) {
        return (IAssetToBusinessWrapper<B>) CONTEXT.get(assetType);
    }

}
