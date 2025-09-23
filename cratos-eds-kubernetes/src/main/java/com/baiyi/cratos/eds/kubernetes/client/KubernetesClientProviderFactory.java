package com.baiyi.cratos.eds.kubernetes.client;

import com.baiyi.cratos.eds.kubernetes.client.provider.BaseKubernetesClientProvider;
import com.baiyi.cratos.eds.kubernetes.enums.KubernetesProvidersEnum;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/3/27 11:19
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class KubernetesClientProviderFactory {

    private static final Map<String, BaseKubernetesClientProvider> CONTEXT = new ConcurrentHashMap<>();

    public static void register(BaseKubernetesClientProvider providerBean) {
        CONTEXT.put(providerBean.getName(), providerBean);
    }

    public static BaseKubernetesClientProvider getProvider(String name) {
        return CONTEXT.get(name);
    }

    public static BaseKubernetesClientProvider getDefaultProvider() {
        return CONTEXT.get(KubernetesProvidersEnum.DEFAULT.getDisplayName());
    }

}
