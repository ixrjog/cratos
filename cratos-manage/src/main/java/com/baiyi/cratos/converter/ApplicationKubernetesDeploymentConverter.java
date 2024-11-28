package com.baiyi.cratos.converter;

import com.baiyi.cratos.common.exception.KubernetesResourceTemplateException;
import com.baiyi.cratos.common.util.IdentityUtil;
import com.baiyi.cratos.domain.generator.ApplicationResource;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.view.application.kubernetes.KubernetesCommonVO;
import com.baiyi.cratos.domain.view.application.kubernetes.KubernetesDeploymentVO;
import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.kubernetes.repo.KubernetesPodRepo;
import com.baiyi.cratos.eds.kubernetes.repo.template.KubernetesDeploymentRepo;
import com.baiyi.cratos.facade.application.builder.KubernetesDeploymentBuilder;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.EdsInstanceService;
import com.google.api.client.util.Maps;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/21 10:11
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationKubernetesDeploymentConverter {

    private final EdsAssetService edsAssetService;
    private final EdsInstanceService edsInstanceService;
    private final EdsInstanceProviderHolderBuilder holderBuilder;
    private final KubernetesDeploymentRepo kubernetesDeploymentRepo;
    private final KubernetesPodRepo kubernetesPodRepo;

    public List<KubernetesDeploymentVO.Deployment> toDeployments(List<ApplicationResource> resources) {
        Map<Integer, EdsKubernetesConfigModel.Kubernetes> edsInstanceConfigMap = Maps.newHashMap();
        return resources.stream()
                .map(e -> to(edsInstanceConfigMap, e))
                .toList();
    }

    private KubernetesDeploymentVO.Deployment to(Map<Integer, EdsKubernetesConfigModel.Kubernetes> edsInstanceConfigMap,
                                                 ApplicationResource resource) {
        int assetId = resource.getBusinessId();
        EdsAsset edsAsset = edsAssetService.getById(assetId);
        EdsInstance edsInstance = edsInstanceService.getById(edsAsset.getInstanceId());
        EdsKubernetesConfigModel.Kubernetes kubernetes = getEdsConfig(edsInstanceConfigMap, edsInstance);
        final String namespace = resource.getNamespace();
        Deployment deployment = kubernetesDeploymentRepo.get(kubernetes, namespace, resource.getName());
        List<Pod> pods = getPods(kubernetes, deployment, namespace);
        KubernetesCommonVO.KubernetesCluster kubernetesCluster = KubernetesCommonVO.KubernetesCluster.builder()
                .name(edsInstance.getInstanceName())
                .build();
        return KubernetesDeploymentBuilder.newBuilder()
                .withKubernetes(kubernetesCluster)
                .withDeployment(deployment)
                .withPods(pods)
                .build();
    }

    private List<Pod> getPods(EdsKubernetesConfigModel.Kubernetes kubernetes, Deployment deployment, String namespace) {
        Map<String, String> labels = deployment.getSpec()
                .getTemplate()
                .getMetadata()
                .getLabels();
        return kubernetesPodRepo.list(kubernetes, namespace, labels);
    }

    private EdsKubernetesConfigModel.Kubernetes getEdsConfig(
            Map<Integer, EdsKubernetesConfigModel.Kubernetes> edsInstanceConfigMap, EdsInstance edsInstance) {
        Optional.ofNullable(edsInstance)
                .map(EdsInstance::getId)
                .orElseThrow(() -> new KubernetesResourceTemplateException("kubernetesInstance is null."));
        if (edsInstanceConfigMap.containsKey(edsInstance.getId())) {
            return edsInstanceConfigMap.get(edsInstance.getId());
        }
        if (IdentityUtil.hasIdentity(edsInstance.getConfigId())) {
            return getEdsInstanceProvider(edsInstance.getId()).getInstance()
                    .getEdsConfigModel();
        }
        throw new KubernetesResourceTemplateException("kubernetesInstance is invalid.");
    }

    @SuppressWarnings("unchecked")
    private EdsInstanceProviderHolder<EdsKubernetesConfigModel.Kubernetes, Deployment> getEdsInstanceProvider(
            int instanceId) {
        EdsInstance edsInstance = edsInstanceService.getById(instanceId);
        EdsAssetTypeEnum edsAssetTypeEnum = EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT;
        return (EdsInstanceProviderHolder<EdsKubernetesConfigModel.Kubernetes, Deployment>) holderBuilder.newHolder(
                instanceId, edsAssetTypeEnum.name());
    }

}
