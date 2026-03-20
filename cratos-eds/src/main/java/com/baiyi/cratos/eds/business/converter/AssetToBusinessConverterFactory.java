package com.baiyi.cratos.eds.business.converter;

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
public class AssetToBusinessConverterFactory {

    private static final Map<String, AssetToBusinessConverter<?>> CONTEXT = new ConcurrentHashMap<>();

    public static <B extends ToBusinessTarget> void register(AssetToBusinessConverter<B> providerBean) {
        log.info("AssetToBusinessProviderFactory Registered: assetType={}", providerBean.getAssetType());
        CONTEXT.put(providerBean.getAssetType(), providerBean);
    }

    @SuppressWarnings("unchecked")
    public static <B extends ToBusinessTarget> AssetToBusinessConverter<B> getProvider(String assetType) {
        return (AssetToBusinessConverter<B>) CONTEXT.get(assetType);
    }

}
