package com.baiyi.cratos.common.cred;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2024/2/29 14:36
 * @Version 1.0
 */
@Slf4j
public class CredProviderFactory {

    private CredProviderFactory() {
    }

    private static final Map<String, ICredProvider> CONTEXT = new ConcurrentHashMap<>();

    public static void register(ICredProvider cred) {
        CONTEXT.put(cred.getType()
                .name(), cred);
        log.debug("CredProviderFactory Registered: credType={}", cred.getType()
                .name());
    }

    public static ICredProvider getCredProvider(String credType) {
        if (CONTEXT.containsKey(credType)) {
            return CONTEXT.get(credType);
        }
        return null;
    }

}
