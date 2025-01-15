package com.baiyi.cratos.converter.impl;

import com.baiyi.cratos.converter.base.BaseKubernetesResourceConverter;
import com.baiyi.cratos.domain.generator.ApplicationResource;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.generator.Env;
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
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;

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
        List<Pod> pods = getPods(kubernetes, deployment);
        KubernetesCommonVO.KubernetesCluster kubernetesCluster = KubernetesCommonVO.KubernetesCluster.builder()
                .name(edsInstance.getInstanceName())
                .build();
        Env env = envService.getByEnvName(namespace);
        return KubernetesDeploymentBuilder.newBuilder()
                .withKubernetes(kubernetesCluster)
                .withDeployment(deployment)
                .withPods(pods)
                .withEnv(env)
                .build();
    }

    private List<Pod> getPods(EdsKubernetesConfigModel.Kubernetes kubernetes, Deployment deployment) {
        return kubernetesPodRepo.listByReplicaSet(kubernetes, deployment);
    }

    protected EdsAssetTypeEnum getEdsAssetType() {
        return EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT;
    }

}
