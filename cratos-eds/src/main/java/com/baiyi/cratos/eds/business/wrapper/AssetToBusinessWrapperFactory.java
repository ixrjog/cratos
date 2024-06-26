package com.baiyi.cratos.eds.business.wrapper;

import com.baiyi.cratos.domain.view.ToBusinessTarget;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/3/12 10:13
 * @Version 1.0
 */
@Slf4j
@NoArgsConstructor(access = PRIVATE)
public class AssetToBusinessWrapperFactory {

    private static final Map<String, IAssetToBusinessWrapper<?>> CONTEXT = new ConcurrentHashMap<>();

    public static <B extends ToBusinessTarget> void register(IAssetToBusinessWrapper<B> providerBean) {
        log.info("AssetToBusinessProviderFactory Registered: assetType={}", providerBean.getAssetType());
        CONTEXT.put(providerBean.getAssetType(), providerBean);
    }

    @SuppressWarnings("unchecked")
    public static <B extends ToBusinessTarget> IAssetToBusinessWrapper<B> getProvider(String assetType) {
        return (IAssetToBusinessWrapper<B>) CONTEXT.get(assetType);
    }

}
