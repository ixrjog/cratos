package com.baiyi.cratos.service.factory.credential;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/2/6 10:00
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class CredentialHolderFactory {

    @Getter
    private static final Map<String, ICredentialHolder> CONTEXT = new ConcurrentHashMap<>();

    public static ICredentialHolder getByName(String name) {
        return CONTEXT.get(name);
    }

    public static void register(ICredentialHolder bean) {
        CONTEXT.put(bean.getName(), bean);
    }

    public static int countByCredentialId(int credentialId) {
        return CONTEXT.values()
                .stream()
                .mapToInt(holder -> holder.countByCredentialId(credentialId))
                .sum();
    }

}
