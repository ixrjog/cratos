package com.baiyi.cratos.eds.kubernetes.client.provider;

import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.kubernetes.client.KubernetesClientBuilder;
import com.baiyi.cratos.eds.kubernetes.client.provider.generator.AmazonEksTokenGenerator;
import com.baiyi.cratos.eds.kubernetes.enums.KubernetesProvidersEnum;
import com.baiyi.cratos.eds.kubernetes.exception.KubernetesException;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2022/9/14 09:37
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AmazonEksClientProvider implements IKubernetesClientProvider {

    private final AmazonEksTokenGenerator amazonEksGenerator;

    public static final String KUBERNETES_REQUEST_TIMEOUT_SYSTEM_PROPERTY = "kubernetes.request.timeout";
    public static final String KUBERNETES_WEBSOCKET_TIMEOUT_SYSTEM_PROPERTY = "kubernetes.websocket.timeout";
    public static final String KUBERNETES_CONNECTION_TIMEOUT_SYSTEM_PROPERTY = "kubernetes.connection.timeout";

    /**
     * 按供应商构建 client
     *
     * @param kubernetes
     * @return
     */
    public KubernetesClient buildClient(EdsKubernetesConfigModel.Kubernetes kubernetes) {
        setProperties(kubernetes);
        io.fabric8.kubernetes.client.Config config = buildConfig(kubernetes);

        return new io.fabric8.kubernetes.client.KubernetesClientBuilder().withConfig(config)
                .build();
    }

    public io.fabric8.kubernetes.client.Config buildConfig(EdsKubernetesConfigModel.Kubernetes kubernetes) {
        try {
            String token = amazonEksGenerator.generateEksToken(kubernetes.getAmazonEks());
            String masterUrl = Optional.of(kubernetes)
                    .map(EdsKubernetesConfigModel.Kubernetes::getAmazonEks)
                    .map(EdsKubernetesConfigModel.AmazonEks::getUrl)
                    .orElseThrow(() -> new KubernetesException("Not configured: AmazonEks -> url"));
            return new ConfigBuilder().withMasterUrl(masterUrl)
                    .withOauthToken(token)
                    .withTrustCerts(true)
                    .withWatchReconnectInterval(60000)
                    .build();
        } catch (URISyntaxException e) {
            throw new KubernetesException(e.getMessage());
        }
    }

    /**
     * 注入配置
     *
     * @param kubernetes
     */
    private void setProperties(EdsKubernetesConfigModel.Kubernetes kubernetes) {
        System.setProperty(KUBERNETES_REQUEST_TIMEOUT_SYSTEM_PROPERTY, String.valueOf(KubernetesClientBuilder.Values.REQUEST_TIMEOUT));
        System.setProperty(KUBERNETES_WEBSOCKET_TIMEOUT_SYSTEM_PROPERTY, String.valueOf(KubernetesClientBuilder.Values.WEBSOCKET_TIMEOUT));
        System.setProperty(KUBERNETES_CONNECTION_TIMEOUT_SYSTEM_PROPERTY, String.valueOf(KubernetesClientBuilder.Values.CONNECTION_TIMEOUT));
    }

    @Override
    public String getName() {
        return KubernetesProvidersEnum.AMAZON_EKS.getDisplayName();
    }

}