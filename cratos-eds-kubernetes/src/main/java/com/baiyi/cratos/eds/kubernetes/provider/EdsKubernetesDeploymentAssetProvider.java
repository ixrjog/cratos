package com.baiyi.cratos.eds.kubernetes.provider;

import com.baiyi.cratos.common.enums.TimeZoneEnum;
import com.baiyi.cratos.common.util.StringFormatter;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.kubernetes.provider.base.BaseEdsKubernetesAssetProvider;
import com.baiyi.cratos.eds.kubernetes.repo.KubernetesDeploymentRepo;
import com.baiyi.cratos.eds.kubernetes.repo.KubernetesNamespaceRepo;
import com.baiyi.cratos.service.EdsAssetIndexService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.baiyi.cratos.common.constant.Global.APP_NAME;

/**
 * @Author baiyi
 * @Date 2024/3/7 10:26
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
@EdsInstanceAssetType(instanceType = EdsInstanceTypeEnum.KUBERNETES, assetType = EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT)
public class EdsKubernetesDeploymentAssetProvider extends BaseEdsKubernetesAssetProvider<Deployment> {

    private final KubernetesNamespaceRepo kubernetesNamespaceRepo;

    private final KubernetesDeploymentRepo kubernetesDeploymentRepo;

    private final EdsAssetIndexService edsAssetIndexService;

    @Override
    protected List<Deployment> listEntities(ExternalDataSourceInstance<EdsKubernetesConfigModel.Kubernetes> instance) throws EdsQueryEntitiesException {
        List<Namespace> namespaces = kubernetesNamespaceRepo.list(instance.getEdsConfigModel());
        List<Deployment> entities = Lists.newArrayList();
        namespaces.forEach(e -> entities.addAll(kubernetesDeploymentRepo.list(instance.getEdsConfigModel(), e.getMetadata()
                .getName())));
        return entities;
    }

    @Override
    protected EdsAsset toEdsAsset(ExternalDataSourceInstance<EdsKubernetesConfigModel.Kubernetes> instance, Deployment entity) {
        final String namespace = entity.getMetadata()
                .getNamespace();
        final String name = entity.getMetadata()
                .getName();
        /*
         * 为了兼容多集群中deployment名称相同导致无法拉取资产
         * 资产id使用联合键 namespace:deployment.name
         */
        final String assetId = getAssetId(namespace, name);

        return newEdsAssetBuilder(instance, entity).assetIdOf(assetId)
                .nameOf(name)
                .kindOf(entity.getKind())
                .createdTimeOf(toUTCDate(entity.getMetadata()
                        .getCreationTimestamp()))
                .build();
    }

    @Override
    protected List<EdsAssetIndex> toEdsAssetIndexList(EdsAsset edsAsset, Deployment entity) {
        List<EdsAssetIndex> indices = Lists.newArrayList();

        Map<String, String> labels = Optional.of(entity)
                .map(Deployment::getMetadata)
                .map(ObjectMeta::getLabels)
                .orElse(Maps.newHashMap());

        String env = "";
        if (labels.containsKey("env")) {
            env = labels.get("env");
            EdsAssetIndex envIndex = EdsAssetIndex.builder()
                    .instanceId(edsAsset.getInstanceId())
                    .assetId(edsAsset.getId())
                    .name("env")
                    .value(env)
                    .build();
            indices.add(envIndex);
        }

        if (labels.containsKey("app")) {
            String appName = labels.get("app");
            if (StringUtils.hasText(env)) {
                // 去掉环境后缀
                if (appName.endsWith("-" + env)) {
                    appName = StringFormatter.eraseLastStr(appName, "-" + env);
                }
            }
            EdsAssetIndex appNameIndex = EdsAssetIndex.builder()
                    .instanceId(edsAsset.getInstanceId())
                    .assetId(edsAsset.getId())
                    .name(APP_NAME)
                    .value(appName)
                    .build();
            indices.add(appNameIndex);
        }

        int replicas = Optional.of(entity)
                .map(Deployment::getSpec)
                .map(DeploymentSpec::getReplicas)
                .orElse(0);
        EdsAssetIndex envIndex = EdsAssetIndex.builder()
                .instanceId(edsAsset.getInstanceId())
                .assetId(edsAsset.getId())
                .name("replicas")
                .value(String.valueOf(replicas))
                .build();
        indices.add(envIndex);
        return indices;
    }

    private Date toUTCDate(String time) {
        return com.baiyi.cratos.common.util.TimeUtil.toDate(time, TimeZoneEnum.UTC);
    }

}
