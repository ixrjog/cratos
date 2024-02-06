package com.baiyi.cratos.service.factory.credential;

import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2024/2/6 10:00
 * @Version 1.0
 */
public class CredentialHolderFactory {

    private CredentialHolderFactory() {
    }

    @Getter
    private static final Map<String, ICredentialHolder> CONTEXT = new ConcurrentHashMap<>();

    public static ICredentialHolder getName(String name) {
        return CONTEXT.get(name);
    }

    public static void register(ICredentialHolder bean) {
        CONTEXT.put(bean.getName(),bean);
    }

    public static int countByCredentialId(int credentialId) {
        return CONTEXT.keySet().stream().mapToInt(k -> CONTEXT.get(k).countByCredentialId(credentialId)).sum();
    }

}
