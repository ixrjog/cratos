package com.baiyi.cratos.eds.kubernetes.client.provider;

import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.core.util.SystemEnvUtil;
import com.baiyi.cratos.eds.kubernetes.client.KubernetesClientBuilder;
import com.baiyi.cratos.eds.kubernetes.enums.KubernetesProvidersEnum;
import com.google.common.base.Joiner;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2022/9/14 09:40
 * @Version 1.0
 */
@Component
public class DefaultKubernetesClientProvider implements IKubernetesClientProvider {

    /**
     * io.fabric8.kubernetes.client.Config.*
     */
    public static final String KUBERNETES_KUBECONFIG_FILE = "kubeconfig";
    public static final String KUBERNETES_REQUEST_TIMEOUT_SYSTEM_PROPERTY = "kubernetes.request.timeout";
    public static final String KUBERNETES_WEBSOCKET_TIMEOUT_SYSTEM_PROPERTY = "kubernetes.websocket.timeout";
    public static final String KUBERNETES_CONNECTION_TIMEOUT_SYSTEM_PROPERTY = "kubernetes.connection.timeout";

    /**
     * 5.0
     * return new DefaultKubernetesClient(config);
     *
     * @param kubernetes
     * @return
     */
    public KubernetesClient buildClient(EdsKubernetesConfigModel.Kubernetes kubernetes) {
        return new io.fabric8.kubernetes.client.KubernetesClientBuilder().withConfig(buildConfig(kubernetes))
                .build();
    }

    public io.fabric8.kubernetes.client.Config buildConfig(EdsKubernetesConfigModel.Kubernetes kubernetes) {
        setProperties(kubernetes);
        return new ConfigBuilder().withTrustCerts(true)
                // .withWebsocketTimeout(KubeClient.Config.WEBSOCKET_TIMEOUT)
                // .withConnectionTimeout(KubeClient.Config.CONNECTION_TIMEOUT)
                // .withRequestTimeout(KubeClient.Config.REQUEST_TIMEOUT)
                .build();
    }

    private void setProperties(EdsKubernetesConfigModel.Kubernetes kubernetes) {
        System.setProperty(KUBERNETES_KUBECONFIG_FILE, toKubeconfigPath(kubernetes));
        System.setProperty(KUBERNETES_REQUEST_TIMEOUT_SYSTEM_PROPERTY, String.valueOf(KubernetesClientBuilder.Values.REQUEST_TIMEOUT));
        System.setProperty(KUBERNETES_WEBSOCKET_TIMEOUT_SYSTEM_PROPERTY, String.valueOf(KubernetesClientBuilder.Values.WEBSOCKET_TIMEOUT));
        System.setProperty(KUBERNETES_CONNECTION_TIMEOUT_SYSTEM_PROPERTY, String.valueOf(KubernetesClientBuilder.Values.CONNECTION_TIMEOUT));
    }

    private static String toKubeconfigPath(EdsKubernetesConfigModel.Kubernetes kubernetes) {
        String kubeConfigPath = Optional.ofNullable(kubernetes)
                .map(EdsKubernetesConfigModel.Kubernetes::getKubeconfig)
                .map(EdsKubernetesConfigModel.Kubeconfig::getPath)
                .orElse("");
        String path = Joiner.on("/")
                .join(kubeConfigPath, io.fabric8.kubernetes.client.Config.KUBERNETES_KUBECONFIG_FILE);
        return SystemEnvUtil.renderEnvHome(path);
    }

    @Override
    public String getName() {
        return KubernetesProvidersEnum.DEFAULT.getDisplayName();
    }

}