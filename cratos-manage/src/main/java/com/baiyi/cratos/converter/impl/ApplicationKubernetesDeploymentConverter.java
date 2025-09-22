package com.baiyi.cratos.converter.impl;

import com.baiyi.cratos.annotation.BusinessWrapper;
import com.baiyi.cratos.converter.base.BaseKubernetesResourceConverter;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.ApplicationResource;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.view.application.kubernetes.KubernetesDeploymentVO;
import com.baiyi.cratos.domain.view.application.kubernetes.common.KubernetesCommonVO;
import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.kubernetes.repo.KubernetesPodRepo;
import com.baiyi.cratos.eds.kubernetes.repo.template.KubernetesDeploymentRepo;
import com.baiyi.cratos.facade.application.builder.KubernetesDeploymentBuilder;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.service.EnvService;
import com.google.api.client.util.Maps;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodTemplateSpec;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentSpec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/21 10:11
 * &#064;Version 1.0
 */
@Slf4j
@Component
public class ApplicationKubernetesDeploymentConverter extends BaseKubernetesResourceConverter<KubernetesDeploymentVO.Deployment, Deployment> {

    private final KubernetesDeploymentRepo kubernetesDeploymentRepo;
    private final KubernetesPodRepo kubernetesPodRepo;


    public ApplicationKubernetesDeploymentConverter(EdsInstanceService edsInstanceService,
                                                    EdsInstanceProviderHolderBuilder holderBuilder,
                                                    EdsAssetService edsAssetService,
                                                    KubernetesDeploymentRepo kubernetesDeploymentRepo,
                                                    KubernetesPodRepo kubernetesPodRepo, EnvService envService) {
        super(edsInstanceService, holderBuilder, edsAssetService, envService);
        this.kubernetesDeploymentRepo = kubernetesDeploymentRepo;
        this.kubernetesPodRepo = kubernetesPodRepo;
    }

    @Override
    public List<KubernetesDeploymentVO.Deployment> toResourceVO(List<ApplicationResource> resources) {
        Map<Integer, EdsKubernetesConfigModel.Kubernetes> edsInstanceConfigMap = Maps.newHashMap();
        return resources.stream()
                .map(resource -> to(edsInstanceConfigMap, resource))
                .filter(Objects::nonNull)
                // 按名称排序
                .sorted()
                .toList();
    }

    private KubernetesDeploymentVO.Deployment to(Map<Integer, EdsKubernetesConfigModel.Kubernetes> edsInstanceConfigMap,
                                                 ApplicationResource resource) {
        int assetId = resource.getBusinessId();
        EdsAsset edsAsset = edsAssetService.getById(assetId);
        if (Objects.isNull(edsAsset)) {
            return null;
        }
        EdsInstance edsInstance = edsInstanceService.getById(edsAsset.getInstanceId());
        EdsKubernetesConfigModel.Kubernetes kubernetes = getEdsConfig(edsInstanceConfigMap, edsInstance);
        final String namespace = resource.getNamespace();
        Deployment deployment = kubernetesDeploymentRepo.get(kubernetes, namespace, resource.getName());
        if (Objects.isNull(deployment)) {
            return null;
        }
        Map<String, String> labels = Optional.of(deployment)
                .map(Deployment::getSpec)
                .map(DeploymentSpec::getTemplate)
                .map(PodTemplateSpec::getMetadata)
                .map(ObjectMeta::getLabels)
                .orElse(Map.of());
        List<Pod> pods;
        if (labels.containsKey("group")) {
            pods = kubernetesPodRepo.list(kubernetes, namespace,
                    Map.of("app", labels.get("app"), "group", labels.get("group")));
        } else {
            pods = getPods(kubernetes, deployment);
        }
        KubernetesCommonVO.KubernetesCluster kubernetesCluster = KubernetesCommonVO.KubernetesCluster.builder()
                .name(edsInstance.getInstanceName())
                .build();
        KubernetesDeploymentVO.Deployment vo = KubernetesDeploymentBuilder.newBuilder()
                .withAssetId(assetId)
                .withKubernetes(kubernetesCluster)
                .withDeployment(deployment)
                .withPods(pods)
                .withEnvName(namespace)
                .build();
        ((ApplicationKubernetesDeploymentConverter) AopContext.currentProxy()).wrap(vo);
        return vo;
    }

    @BusinessWrapper(types = {BusinessTypeEnum.BUSINESS_TAG, BusinessTypeEnum.ENV})
    public void wrap(KubernetesDeploymentVO.Deployment vo) {
    }

    private List<Pod> getPods(EdsKubernetesConfigModel.Kubernetes kubernetes, Deployment deployment) {
        return kubernetesPodRepo.listByReplicaSet(kubernetes, deployment);
    }

    protected EdsAssetTypeEnum getEdsAssetType() {
        return EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT;
    }

}
