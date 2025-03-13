package com.baiyi.cratos.facade.identity.extension.mail;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/13 10:34
 * &#064;Version 1.0
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MailIdentityFactory {

    private static final Map<String, MailIdentityProvider> CONTEXT = new ConcurrentHashMap<>();

    public static void register(MailIdentityProvider providerBean) {
        log.info("MailIdentityFactory Registered: instanceType={}", providerBean.getInstanceType());
        CONTEXT.put(providerBean.getInstanceType(), providerBean);
    }

    public static MailIdentityProvider getProvider(String instanceType) {
        if (CONTEXT.containsKey(instanceType)) {
            return CONTEXT.get(instanceType);
        }
        throw new IllegalArgumentException("Incorrect instanceType: " + instanceType);
    }

}
