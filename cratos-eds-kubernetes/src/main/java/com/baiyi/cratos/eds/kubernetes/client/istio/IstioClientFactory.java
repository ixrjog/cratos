package com.baiyi.cratos.eds.kubernetes.client.istio;

import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.kubernetes.client.KubernetesClientProviderFactory;
import com.baiyi.cratos.eds.kubernetes.client.provider.IKubernetesClientProvider;
import com.baiyi.cratos.eds.kubernetes.exception.KubernetesException;
import io.fabric8.istio.client.DefaultIstioClient;
import io.fabric8.istio.client.IstioClient;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author baiyi
 * @Date 2023/10/7 15:28
 * @Version 1.0
 */
public class IstioClientFactory {

    private IstioClientFactory() {
        throw new IllegalStateException("Utility class.");
    }

    public static IstioClient newClient(EdsKubernetesConfigModel.Kubernetes kubernetes) {
        io.fabric8.kubernetes.client.Config cfg = buildConfig(kubernetes);
        return new DefaultIstioClient(cfg);
    }

    public static io.fabric8.kubernetes.client.Config buildConfig(EdsKubernetesConfigModel.Kubernetes kubernetes) {
        if (StringUtils.isNotBlank(kubernetes.getProvider())) {
            IKubernetesClientProvider kubernetesClientProvider = KubernetesClientProviderFactory.getProvider(kubernetes.getProvider());
            if (kubernetesClientProvider == null) {
                throw new KubernetesException("Invalid provider {}", kubernetes.getProvider());
            }
            return kubernetesClientProvider.buildConfig(kubernetes);
        }
        return KubernetesClientProviderFactory.getDefaultProvider()
                .buildConfig(kubernetes);
    }

}