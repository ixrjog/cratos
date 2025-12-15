package com.baiyi.cratos.eds.kubernetes.client.istio;

import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.kubernetes.client.KubernetesClientProviderFactory;
import com.baiyi.cratos.eds.kubernetes.client.provider.BaseKubernetesClientProvider;
import com.baiyi.cratos.eds.kubernetes.exception.KubernetesException;
import io.fabric8.istio.client.DefaultIstioClient;
import io.fabric8.istio.client.IstioClient;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2023/10/7 15:28
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class IstioClientFactory {

    public static IstioClient newClient(EdsConfigs.Kubernetes kubernetes) {
        io.fabric8.kubernetes.client.Config cfg = buildConfig(kubernetes);
        return new DefaultIstioClient(cfg);
    }

    public static io.fabric8.kubernetes.client.Config buildConfig(EdsConfigs.Kubernetes kubernetes) {
        if (StringUtils.isNotBlank(kubernetes.getProvider())) {
            BaseKubernetesClientProvider kubernetesClientProvider = KubernetesClientProviderFactory.getProvider(kubernetes.getProvider());
            if (kubernetesClientProvider == null) {
                throw new KubernetesException("Invalid provider {}", kubernetes.getProvider());
            }
            return kubernetesClientProvider.buildConfig(kubernetes);
        }
        return KubernetesClientProviderFactory.getDefaultProvider()
                .buildConfig(kubernetes);
    }

}