package com.baiyi.cratos.eds.core.update;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/6/7 上午11:23
 * &#064;Version 1.0
 */
@Slf4j
public class UpdateBusinessFromAssetProviderFactory {

    private UpdateBusinessFromAssetProviderFactory() {
    }

    private static final Map<String, IUpdateBusinessFromAssetProvider> CONTEXT = new ConcurrentHashMap<>();

    public static void register(IUpdateBusinessFromAssetProvider providerBean) {
        log.info("UpdateBusinessFromAssetProviderFactory Registered: businessType={}", providerBean.getBusinessType());
        CONTEXT.put(providerBean.getBusinessType(), providerBean);
    }

    public static IUpdateBusinessFromAssetProvider getProvider(String businessType) {
        return CONTEXT.get(businessType);
    }

}
