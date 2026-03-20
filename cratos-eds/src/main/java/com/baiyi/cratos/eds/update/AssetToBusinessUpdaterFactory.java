package com.baiyi.cratos.eds.update;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/6/7 上午11:23
 * &#064;Version 1.0
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AssetToBusinessUpdaterFactory {
    
    private static final Map<String, AssetToBusinessUpdater> CONTEXT = new ConcurrentHashMap<>();

    public static void register(AssetToBusinessUpdater providerBean) {
        log.info("AssetToBusinessUpdaterFactory Registered: businessType={}", providerBean.getBusinessType());
        CONTEXT.put(providerBean.getBusinessType(), providerBean);
    }

    public static AssetToBusinessUpdater getUpdater(String businessType) {
        return CONTEXT.get(businessType);
    }

}
