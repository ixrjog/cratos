package com.baiyi.cratos.eds.kubernetes.client.provider;

import com.baiyi.cratos.eds.core.config.model.EdsKubernetesConfigModel;
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
public class AmazonEksClientProvider implements BaseKubernetesClientProvider {

    private final AmazonEksTokenGenerator amazonEksGenerator;

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
                    .withRequestTimeout(60000)
                    .build();
        } catch (URISyntaxException e) {
            throw new KubernetesException(e.getMessage());
        }
    }

    @Override
    public String getName() {
        return KubernetesProvidersEnum.AMAZON_EKS.getDisplayName();
    }

}