package com.baiyi.cratos.common.cred;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/2/29 14:36
 * @Version 1.0
 */
@Slf4j
@NoArgsConstructor(access = PRIVATE)
public class CredProviderFactory {

    private static final Map<String, BaseCredProvider> CONTEXT = new ConcurrentHashMap<>();

    public static void register(BaseCredProvider cred) {
        CONTEXT.put(cred.getType()
                .name(), cred);
        log.debug("CredProviderFactory Registered: credType={}", cred.getType()
                .name());
    }

    public static BaseCredProvider getCredProvider(String credType) {
        if (CONTEXT.containsKey(credType)) {
            return CONTEXT.get(credType);
        }
        return null;
    }

}
