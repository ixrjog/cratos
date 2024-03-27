package com.baiyi.cratos.eds.kubernetes.client;

import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.kubernetes.client.provider.IKubernetesClientProvider;
import com.baiyi.cratos.eds.kubernetes.exception.KubernetesException;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/6/24 5:07 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class KubernetesClientBuilder {

    public interface Values {
        int CONNECTION_TIMEOUT = 60 * 1000;
        int REQUEST_TIMEOUT = 60 * 1000;
        int WEBSOCKET_TIMEOUT = 60 * 1000;
    }

    public KubernetesClient build(EdsKubernetesConfigModel.Kubernetes kubernetes) {
        if (StringUtils.isNotBlank(kubernetes.getProvider())) {
            IKubernetesClientProvider kubernetesClientProvider = KubernetesClientProviderFactory.getProvider(kubernetes.getProvider());
            if (kubernetesClientProvider == null) {
                throw new KubernetesException("Invalid provider {}", kubernetes.getProvider());
            }
            return kubernetesClientProvider.buildClient(kubernetes);
        }
        return KubernetesClientProviderFactory.getDefaultProvider()
                .buildClient(kubernetes);
    }

}