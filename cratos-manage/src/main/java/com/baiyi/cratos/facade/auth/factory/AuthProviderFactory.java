package com.baiyi.cratos.facade.auth.factory;

import com.baiyi.cratos.facade.auth.IAuthProvider;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2024/1/10 13:47
 * @Version 1.0
 */
@Slf4j
public class AuthProviderFactory {

    private AuthProviderFactory() {
    }

    private static final Map<String, IAuthProvider> CONTEXT = new ConcurrentHashMap<>();

    public static void register(IAuthProvider bean) {
        CONTEXT.put(bean.getName(), bean);
        log.debug("AuthProviderFactory Registered: {}", bean.getName());
    }

    public static IAuthProvider getProvider(String name) {
        if (CONTEXT.containsKey(name)) {
            return CONTEXT.get(name);
        }
        return null;
    }

}
