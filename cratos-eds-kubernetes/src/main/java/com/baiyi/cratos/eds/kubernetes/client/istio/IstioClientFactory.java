package com.baiyi.cratos.eds.kubernetes.client.istio;

import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.kubernetes.client.provider.DefaultKubernetesProvider;
import io.fabric8.istio.client.DefaultIstioClient;
import io.fabric8.istio.client.IstioClient;

/**
 * @Author baiyi
 * @Date 2023/10/7 15:28
 * @Version 1.0
 */
public class IstioClientFactory {

    private IstioClientFactory() {
        throw new IllegalStateException("Utility class");
    }

    public static IstioClient newClient(EdsKubernetesConfigModel.Kubernetes kubernetes) {
        DefaultKubernetesProvider.buildConfig(kubernetes);
        io.fabric8.kubernetes.client.Config cfg = DefaultKubernetesProvider.buildConfig(kubernetes);
        return new DefaultIstioClient(cfg);
    }

}