package com.baiyi.cratos.eds.kubernetes.client;

import com.baiyi.cratos.eds.kubernetes.client.provider.IKubernetesClientProvider;
import com.baiyi.cratos.eds.kubernetes.enums.KubernetesProvidersEnum;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author baiyi
 * @Date 2024/3/27 11:19
 * @Version 1.0
 */
public class KubernetesClientProviderFactory {

    private KubernetesClientProviderFactory() {
    }

    private static final Map<String, IKubernetesClientProvider> CONTEXT = new ConcurrentHashMap<>();


    public static void register(IKubernetesClientProvider providerBean) {
        CONTEXT.put(providerBean.getName(), providerBean);
    }

    public static IKubernetesClientProvider getProvider(String name) {
        return CONTEXT.get(name);
    }

    public static IKubernetesClientProvider getDefaultProvider() {
        return CONTEXT.get(KubernetesProvidersEnum.DEFAULT.getDisplayName());
    }

}
