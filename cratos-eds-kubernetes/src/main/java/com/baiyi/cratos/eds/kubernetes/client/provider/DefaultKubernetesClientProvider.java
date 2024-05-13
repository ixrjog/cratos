package com.baiyi.cratos.eds.kubernetes.client.provider;

import com.baiyi.cratos.common.util.IdentityUtil;
import com.baiyi.cratos.domain.generator.Credential;
import com.baiyi.cratos.domain.generator.EdsConfig;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.core.exception.EdsConfigException;
import com.baiyi.cratos.eds.kubernetes.enums.KubernetesProvidersEnum;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsConfigService;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2022/9/14 09:40
 * @Version 1.0
 */
@RequiredArgsConstructor
@Component
public class DefaultKubernetesClientProvider implements IKubernetesClientProvider {

    private final CredentialService credentialService;

    private final EdsConfigService edsConfigService;

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
        // setProperties(kubernetes);
//        return new ConfigBuilder().withTrustCerts(true)
//                // .withWebsocketTimeout(KubeClient.Config.WEBSOCKET_TIMEOUT)
//                // .withConnectionTimeout(KubeClient.Config.CONNECTION_TIMEOUT)
//                // .withRequestTimeout(KubeClient.Config.REQUEST_TIMEOUT)
//                .build();
        int configId = Optional.of(kubernetes)
                .map(EdsKubernetesConfigModel.Kubernetes::getEdsInstance)
                .map(EdsInstance::getConfigId)
                .orElseThrow(() -> new EdsConfigException("No configId specified for kubernetes."));

        EdsConfig edsConfig = edsConfigService.getById(configId);
        if (edsConfig == null) {
            throw new EdsConfigException("No config specified for kubernetes.");
        }
        if (IdentityUtil.hasIdentity(edsConfig.getCredentialId())) {
            Credential kubeconfigCredential = credentialService.getById(edsConfig.getCredentialId());
            return io.fabric8.kubernetes.client.Config.fromKubeconfig(kubeconfigCredential.getCredential());
        } else {
            throw new EdsConfigException("Kubernetes does not specify credential(kubeconfig).");
        }
    }

    @Override
    public void setProperties(EdsKubernetesConfigModel.Kubernetes kubernetes) {
//        System.setProperty(KUBERNETES_KUBECONFIG_FILE, toKubeconfigPath(kubernetes));
//        IKubernetesClientProvider.super.setProperties(kubernetes);
    }

//    private static String toKubeconfigPath(EdsKubernetesConfigModel.Kubernetes kubernetes) {
//        String kubeConfigPath = Optional.ofNullable(kubernetes)
//                .map(EdsKubernetesConfigModel.Kubernetes::getKubeconfig)
//                .map(EdsKubernetesConfigModel.Kubeconfig::getPath)
//                .orElse("");
//        String path = Joiner.on("/")
//                .join(kubeConfigPath, io.fabric8.kubernetes.client.Config.KUBERNETES_KUBECONFIG_FILE);
//        return SystemEnvUtil.renderEnvHome(path);
//    }

    @Override
    public String getName() {
        return KubernetesProvidersEnum.DEFAULT.getDisplayName();
    }

}