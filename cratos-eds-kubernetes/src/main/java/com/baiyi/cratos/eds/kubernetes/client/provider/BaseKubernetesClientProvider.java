package com.baiyi.cratos.eds.kubernetes.client.provider;

import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.kubernetes.client.KubernetesClientBuilder;
import com.baiyi.cratos.eds.kubernetes.client.KubernetesClientProviderFactory;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.springframework.beans.factory.InitializingBean;

/**
 * @Author baiyi
 * @Date 2024/3/27 10:34
 * @Version 1.0
 */
public interface BaseKubernetesClientProvider extends InitializingBean {

    String KUBERNETES_KUBECONFIG_FILE = "kubeconfig";
    // io.fabric8.kubernetes.client.KUBERNETES_REQUEST_TIMEOUT_SYSTEM_PROPERTY

    // String KUBERNETES_REQUEST_TIMEOUT_SYSTEM_PROPERTY = "kubernetes.request.timeout";
    String KUBERNETES_REQUEST_TIMEOUT_SYSTEM_PROPERTY = io.fabric8.kubernetes.client.Config.KUBERNETES_REQUEST_TIMEOUT_SYSTEM_PROPERTY;
    String KUBERNETES_WEBSOCKET_TIMEOUT_SYSTEM_PROPERTY = "kubernetes.websocket.timeout";
    // KUBERNETES_CONNECTION_TIMEOUT_SYSTEM_PROPERTY = "kubernetes.connection.timeout";
    String KUBERNETES_CONNECTION_TIMEOUT_SYSTEM_PROPERTY = io.fabric8.kubernetes.client.Config.KUBERNETES_CONNECTION_TIMEOUT_SYSTEM_PROPERTY;

    String getName();

    io.fabric8.kubernetes.client.Config buildConfig(EdsConfigs.Kubernetes kubernetes);

    KubernetesClient buildClient(EdsConfigs.Kubernetes kubernetes);

    default void setProperties(EdsConfigs.Kubernetes kubernetes) {
        System.setProperty(KUBERNETES_REQUEST_TIMEOUT_SYSTEM_PROPERTY,
                String.valueOf(KubernetesClientBuilder.Values.REQUEST_TIMEOUT));
        System.setProperty(KUBERNETES_WEBSOCKET_TIMEOUT_SYSTEM_PROPERTY,
                String.valueOf(KubernetesClientBuilder.Values.WEBSOCKET_TIMEOUT));
        System.setProperty(KUBERNETES_CONNECTION_TIMEOUT_SYSTEM_PROPERTY,
                String.valueOf(KubernetesClientBuilder.Values.CONNECTION_TIMEOUT));
    }

    @Override
    default void afterPropertiesSet() {
        KubernetesClientProviderFactory.register(this);
    }

}
