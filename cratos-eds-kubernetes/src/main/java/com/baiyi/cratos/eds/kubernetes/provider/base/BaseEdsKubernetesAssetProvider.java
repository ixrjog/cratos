package com.baiyi.cratos.eds.kubernetes.provider.base;

import com.baiyi.cratos.common.enums.CredentialTypeEnum;
import com.baiyi.cratos.common.util.IOUtil;
import com.baiyi.cratos.domain.generator.Credential;
import com.baiyi.cratos.domain.generator.EdsConfig;
import com.baiyi.cratos.eds.core.BaseEdsInstanceAssetProvider;
import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.core.util.SystemEnvUtil;
import com.baiyi.cratos.eds.kubernetes.client.provider.enums.KubernetesProvidersEnum;
import com.baiyi.cratos.service.CredentialService;
import com.google.common.base.Joiner;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2024/3/7 15:41
 * @Version 1.0
 */
@Slf4j
public abstract class BaseEdsKubernetesAssetProvider<A> extends BaseEdsInstanceAssetProvider<EdsKubernetesConfigModel.Kubernetes, A> {

    @Resource
    private CredentialService credentialService;

    @Override
    public void setConfig(EdsConfig edsConfig) {
        if (edsConfig.getCredentialId() == null) {
            return;
        }

        Credential credential = credentialService.getById(edsConfig.getCredentialId());
        if (credential == null || !CredentialTypeEnum.KUBE_CONFIG.name()
                .equals(credential.getCredentialType())) {
            // 凭据为空或类型不匹配
            return;
        }
        EdsKubernetesConfigModel.Kubernetes kubernetes = configLoadAs(edsConfig.getConfigContent());
        if (KubernetesProvidersEnum.AMAZON_EKS.getDesc()
                .equals(Optional.of(kubernetes)
                        .map(EdsKubernetesConfigModel.Kubernetes::getProvider)
                        .orElse(""))) {
            log.debug("No need to set configuration files.");
            return;
        }

        String path = Optional.of(kubernetes)
                .map(EdsKubernetesConfigModel.Kubernetes::getKubeconfig)
                .map(EdsKubernetesConfigModel.Kubeconfig::getPath)
                .orElse("");
        if (StringUtils.hasText(path)) {
            // 注入 ${HOME}
            final String kubeconfigPath = SystemEnvUtil.renderEnvHome(kubernetes.getKubeconfig()
                    .getPath());
            try {
                IOUtil.writeFile(credential.getCredential(), Joiner.on("/")
                        .join(kubeconfigPath, io.fabric8.kubernetes.client.Config.KUBERNETES_KUBECONFIG_FILE));
            } catch (Exception e) {
                log.error("Error writing kubeconfig file: {}", e.getMessage());
                // throw new KubernetesException("Error writing kubeconfig file: {}", e.getMessage());
            }
        }
    }

}