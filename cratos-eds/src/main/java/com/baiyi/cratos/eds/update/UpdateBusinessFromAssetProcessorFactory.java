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
public class UpdateBusinessFromAssetProcessorFactory {

    private UpdateBusinessFromAssetProcessorFactory() {
    }

    private static final Map<String, IUpdateBusinessFromAssetProcessor> CONTEXT = new ConcurrentHashMap<>();

    public static void register(IUpdateBusinessFromAssetProcessor providerBean) {
        log.info("UpdateBusinessFromAssetProcessorFactory Registered: businessType={}", providerBean.getBusinessType());
        CONTEXT.put(providerBean.getBusinessType(), providerBean);
    }

    public static IUpdateBusinessFromAssetProcessor getProvider(String businessType) {
        return CONTEXT.get(businessType);
    }

}
