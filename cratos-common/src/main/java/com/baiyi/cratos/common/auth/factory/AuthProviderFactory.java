package com.baiyi.cratos.common.auth.factory;


import com.baiyi.cratos.common.auth.AuthProvider;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/1/10 13:47
 * @Version 1.0
 */
@Slf4j
@NoArgsConstructor(access = PRIVATE)
public class AuthProviderFactory {

    private static final Map<String, AuthProvider> CONTEXT = new ConcurrentHashMap<>();

    public static void register(AuthProvider bean) {
        CONTEXT.put(bean.getName(), bean);
        log.debug("AuthProviderFactory Registered: {}", bean.getName());
    }

    public static AuthProvider getProvider(String name) {
        if (CONTEXT.containsKey(name)) {
            return CONTEXT.get(name);
        }
        return null;
    }

}
