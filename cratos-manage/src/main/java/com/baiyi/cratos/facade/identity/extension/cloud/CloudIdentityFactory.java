package com.baiyi.cratos.facade.identity.extension.cloud;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/28 11:19
 * &#064;Version 1.0
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CloudIdentityFactory {

    private static final Map<String, CloudIdentityProvider> CONTEXT = new ConcurrentHashMap<>();

    public static void register(CloudIdentityProvider providerBean) {
        log.info("CloudIdentityFactory Registered: instanceType={}", providerBean.getInstanceType());
        CONTEXT.put(providerBean.getInstanceType(), providerBean);
    }

}
