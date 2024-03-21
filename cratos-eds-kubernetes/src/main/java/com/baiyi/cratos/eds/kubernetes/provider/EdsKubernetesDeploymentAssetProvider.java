package com.baiyi.cratos.eds.kubernetes.provider;

import com.baiyi.cratos.common.enums.TimeZoneEnum;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.exception.EdsQueryEntitiesException;
import com.baiyi.cratos.eds.core.support.ExternalDataSourceInstance;
import com.baiyi.cratos.eds.kubernetes.provider.base.BaseEdsKubernetesAssetProvider;
import com.baiyi.cratos.eds.kubernetes.repo.KubernetesDeploymentRepo;
import com.baiyi.cratos.eds.kubernetes.repo.KubernetesNamespaceRepo;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/3/7 10:26
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
@EdsInstanceAssetType(instanceType = EdsInstanceTypeEnum.KUBERNETES, assetType = EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT)
public class EdsKubernetesDeploymentAssetProvider extends BaseEdsKubernetesAssetProvider<Deployment> {

    @Override
    protected List<Deployment> listEntities(ExternalDataSourceInstance<EdsKubernetesConfigModel.Kubernetes> instance) throws EdsQueryEntitiesException {
        List<Namespace> namespaces = KubernetesNamespaceRepo.list(instance.getEdsConfigModel());
        List<Deployment> entities = Lists.newArrayList();
        namespaces.forEach(e -> entities.addAll(KubernetesDeploymentRepo.list(instance.getEdsConfigModel(), e.getMetadata()
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
        final String assetId = Joiner.on(":")
                .join(namespace, name);

        return newEdsAssetBuilder(instance, entity).assetIdOf(assetId)
                .nameOf(name)
                .kindOf(entity.getKind())
                .createdTimeOf(toUTCDate(entity.getMetadata()
                        .getCreationTimestamp()))
                .build();
    }

    private Date toUTCDate(String time) {
        return com.baiyi.cratos.common.util.TimeUtil.toDate(time, TimeZoneEnum.UTC);
    }

}
