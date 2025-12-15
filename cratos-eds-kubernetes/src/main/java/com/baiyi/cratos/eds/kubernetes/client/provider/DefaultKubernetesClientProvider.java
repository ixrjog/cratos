package com.baiyi.cratos.eds.kubernetes.client.provider;

import com.baiyi.cratos.common.util.IdentityUtils;
import com.baiyi.cratos.domain.generator.Credential;
import com.baiyi.cratos.domain.generator.EdsConfig;
import com.baiyi.cratos.eds.core.config.model.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.core.exception.EdsConfigException;
import com.baiyi.cratos.eds.kubernetes.enums.KubernetesProvidersEnum;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsConfigService;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.internal.KubeConfigUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Optional;

import static com.baiyi.cratos.eds.kubernetes.client.KubernetesClientBuilder.Values.CONNECTION_TIMEOUT;
import static com.baiyi.cratos.eds.kubernetes.client.KubernetesClientBuilder.Values.REQUEST_TIMEOUT;
import static io.fabric8.kubernetes.client.Config.empty;

/**
 * @Author baiyi
 * @Date 2022/9/14 09:40
 * @Version 1.0
 */
@RequiredArgsConstructor
@Component
public class DefaultKubernetesClientProvider implements BaseKubernetesClientProvider {

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
        int configId = kubernetes.getConfigId();
        EdsConfig edsConfig = edsConfigService.getById(configId);
        if (edsConfig == null) {
            throw new EdsConfigException("No config specified for kubernetes.");
        }
        if (IdentityUtils.hasIdentity(edsConfig.getCredentialId())) {
            Credential kubeconfigCredential = credentialService.getById(edsConfig.getCredentialId());
            String contextName = Optional.of(kubernetes)
                    .map(EdsKubernetesConfigModel.Kubernetes::getKubeconfig)
                    .map(EdsKubernetesConfigModel.Kubeconfig::getUseContext)
                    .orElse(null);
            io.fabric8.kubernetes.client.Config config;
            if (StringUtils.hasText(contextName)) {
                io.fabric8.kubernetes.api.model.Config kubeconfig = KubeConfigUtils.parseConfigFromString(
                        kubeconfigCredential.getCredential());
                config = empty();
                KubeConfigUtils.merge(config, contextName, kubeconfig);
            } else {
                config = io.fabric8.kubernetes.client.Config.fromKubeconfig(kubeconfigCredential.getCredential());
            }
            config.setConnectionTimeout(CONNECTION_TIMEOUT);
            config.setRequestTimeout(REQUEST_TIMEOUT);
            return config;
        } else {
            throw new EdsConfigException("Kubernetes does not specify credential(kubeconfig).");
        }
    }

    @Override
    public void setProperties(EdsKubernetesConfigModel.Kubernetes kubernetes) {
    }

    @Override
    public String getName() {
        return KubernetesProvidersEnum.DEFAULT.getDisplayName();
    }

}