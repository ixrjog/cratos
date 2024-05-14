package com.baiyi.cratos.eds.kubernetes.provider.base;

import com.baiyi.cratos.common.enums.TimeZoneEnum;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.core.BaseEdsInstanceAssetProvider;
import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.core.facade.EdsAssetIndexFacade;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.facade.SimpleEdsFacade;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.EdsAssetService;
import com.google.common.base.Joiner;
import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2024/3/7 15:41
 * @Version 1.0
 */
@Slf4j
public abstract class BaseEdsKubernetesAssetProvider<A extends HasMetadata> extends BaseEdsInstanceAssetProvider<EdsKubernetesConfigModel.Kubernetes, A> {

    public BaseEdsKubernetesAssetProvider(EdsAssetService edsAssetService, SimpleEdsFacade simpleEdsFacade,
                                          CredentialService credentialService, ConfigCredTemplate configCredTemplate,
                                          EdsAssetIndexFacade edsAssetIndexFacade) {
        super(edsAssetService, simpleEdsFacade, credentialService, configCredTemplate, edsAssetIndexFacade);
    }

    protected String getAssetId(String namespace, String name) {
        return Joiner.on(":")
                .join(namespace, name);
    }

    protected String getAssetId(HasMetadata hasMetadata) {
        return Joiner.on(":").skipNulls()
                .join(getNamespace(hasMetadata), getName(hasMetadata));
    }

    protected String getName(HasMetadata hasMetadata) {
        return hasMetadata.getMetadata()
                .getName();
    }

    protected String getNamespace(HasMetadata hasMetadata) {
        return hasMetadata.getMetadata()
                .getNamespace();
    }

    protected Date getCreationTime(HasMetadata hasMetadata) {
        return toUTCDate(hasMetadata.getMetadata()
                .getCreationTimestamp());
    }

    protected String getMetadataLabel(HasMetadata hasMetadata, String key) {
        Optional<Map<String, String>> optionalLabels = Optional.of(hasMetadata)
                .map(HasMetadata::getMetadata)
                .map(ObjectMeta::getLabels);
        return optionalLabels.map(stringStringMap -> stringStringMap.getOrDefault(key, null))
                .orElse(null);
    }

//    @Override
//    public void setConfig(EdsConfig edsConfig) {
//        if (edsConfig.getCredentialId() == null) {
//            return;
//        }
//
//        Credential credential = credentialService.getById(edsConfig.getCredentialId());
//        if (credential == null || !CredentialTypeEnum.KUBE_CONFIG.name()
//                .equals(credential.getCredentialType())) {
//            // 凭据为空或类型不匹配
//            return;
//        }
//        EdsKubernetesConfigModel.Kubernetes kubernetes = configLoadAs(edsConfig.getConfigContent());
//        if (KubernetesProvidersEnum.AMAZON_EKS.getDisplayName()
//                .equals(Optional.of(kubernetes)
//                        .map(EdsKubernetesConfigModel.Kubernetes::getProvider)
//                        .orElse(""))) {
//            log.debug("No need to set configuration files.");
//            return;
//        }
//
//        String path = Optional.of(kubernetes)
//                .map(EdsKubernetesConfigModel.Kubernetes::getKubeconfig)
//                .map(EdsKubernetesConfigModel.Kubeconfig::getPath)
//                .orElse("");
//        if (StringUtils.hasText(path)) {
//            // 注入 ${HOME}
//            final String kubeconfigPath = SystemEnvUtil.renderEnvHome(kubernetes.getKubeconfig()
//                    .getPath());
//            try {
//                IOUtil.writeFile(credential.getCredential(), Joiner.on("/")
//                        .join(kubeconfigPath, io.fabric8.kubernetes.client.Config.KUBERNETES_KUBECONFIG_FILE));
//            } catch (Exception e) {
//                log.error("Error writing kubeconfig file: {}", e.getMessage());
//                // throw new KubernetesException("Error writing kubeconfig file: {}", e.getMessage());
//            }
//        }
//    }

    @Override
    protected EdsAsset toEdsAsset(ExternalDataSourceInstance<EdsKubernetesConfigModel.Kubernetes> instance, A entity) {
        return newEdsAssetBuilder(instance, entity).assetIdOf(getAssetId(entity))
                .nameOf(getName(entity))
                .kindOf(entity.getKind())
                .createdTimeOf(getCreationTime(entity))
                .build();
    }

    private Date toUTCDate(String time) {
        return com.baiyi.cratos.common.util.TimeUtil.toDate(time, TimeZoneEnum.UTC);
    }

}