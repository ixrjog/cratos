package com.baiyi.cratos.facade.kubernetes.impl;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.domain.generator.KubernetesDeploymentAppVersionComparison;
import com.baiyi.cratos.domain.view.kubernetes.KubernetesDeploymentAppVersionVO;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsProviderHolderFactory;
import com.baiyi.cratos.eds.kubernetes.util.KubeUtils;
import com.baiyi.cratos.facade.kubernetes.KubernetesDeploymentAppVersionComparisonFacade;
import com.baiyi.cratos.service.EdsAssetIndexService;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.KubernetesDeploymentAppVersionComparisonService;
import com.google.common.collect.Maps;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.baiyi.cratos.common.configuration.CachingConfiguration.RepositoryName.VERY_SHORT;
import static com.baiyi.cratos.domain.constant.Global.APP_NAME;
import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.KUBERNETES_NAMESPACE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/6/3 17:09
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
public class KubernetesDeploymentAppVersionComparisonFacadeImpl implements KubernetesDeploymentAppVersionComparisonFacade {

    private final KubernetesDeploymentAppVersionComparisonService comparisonService;
    private final EdsAssetService edsAssetService;
    private final EdsAssetIndexService edsAssetIndexService;
    private final EdsProviderHolderFactory edsProviderHolderFactory;

    @Override
    @Cacheable(cacheNames = VERY_SHORT, key = "'KUBERNETES:DEPLOYMENT:VERSION:COMPARISON:ID:'+ #id", unless = "#result == null")
    public KubernetesDeploymentAppVersionVO.ComparisonVersion compareDeploymentVersion(int id) {
        KubernetesDeploymentAppVersionComparison comparison = comparisonService.getById(id);
        if (comparison == null) {
            throw new IllegalArgumentException("Comparison not found: " + id);
        }
        Map<String, KubernetesDeploymentAppVersionVO.DeploymentAssets> appMap = Maps.newHashMap();
        populateAssetMap(comparison.getDcKubernetesInstanceId(), appMap, comparison.getNamespace(), true);
        populateAssetMap(comparison.getDrKubernetesInstanceId(), appMap, comparison.getNamespace(), false);
        EdsInstanceProviderHolder<EdsConfigs.Kubernetes, Deployment> dcHolder = createHolder(
                comparison.getDcKubernetesInstanceId());
        EdsInstanceProviderHolder<EdsConfigs.Kubernetes, Deployment> drHolder = createHolder(
                comparison.getDrKubernetesInstanceId());

        List<KubernetesDeploymentAppVersionVO.ApplicationVersion> applicationVersions = appMap.entrySet()
                .stream()
                .map(entry -> KubernetesDeploymentAppVersionVO.ApplicationVersion.builder()
                        .appName(entry.getKey())
                        .dcDeploymentImages(buildDeploymentImages(
                                entry.getValue()
                                        .getDcAssets(), dcHolder
                        ))
                        .drDeploymentImages(buildDeploymentImages(
                                entry.getValue()
                                        .getDrAssets(), drHolder
                        ))
                        .build())
                .toList();

        return KubernetesDeploymentAppVersionVO.ComparisonVersion.builder()
                .applicationVersions(applicationVersions)
                .build();
    }

    private void populateAssetMap(int instanceId, Map<String, KubernetesDeploymentAppVersionVO.DeploymentAssets> appMap,
                                  String namespace, boolean isDc) {
        List<EdsAsset> assets = edsAssetService.queryInstanceAssets(
                instanceId, EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name());
        for (EdsAsset asset : assets) {
            EdsAssetIndex appNameIndex = edsAssetIndexService.getByAssetIdAndName(asset.getId(), APP_NAME);
            if (appNameIndex == null) {
                continue;
            }
            EdsAssetIndex namespaceIndex = edsAssetIndexService.getByAssetIdAndName(
                    asset.getId(), KUBERNETES_NAMESPACE);
            if (namespaceIndex == null || !namespace.equals(namespaceIndex.getValue())) {
                continue;
            }
            String appName = appNameIndex.getValue();
            KubernetesDeploymentAppVersionVO.DeploymentAssets deploymentAssets = appMap.computeIfAbsent(
                    appName,
                    k -> KubernetesDeploymentAppVersionVO.DeploymentAssets.builder()
                            .build()
            );
            if (isDc) {
                deploymentAssets.getDcAssets()
                        .add(asset);
            } else {
                deploymentAssets.getDrAssets()
                        .add(asset);
            }
        }
    }

    private List<KubernetesDeploymentAppVersionVO.DeploymentImage> buildDeploymentImages(List<EdsAsset> assets,
                                                                                         EdsInstanceProviderHolder<EdsConfigs.Kubernetes, Deployment> holder) {
        return assets.stream()
                .map(asset -> {
                    try {
                        Deployment deployment = holder.getProvider()
                                .loadAsset(asset.getOriginalModel());
                        Optional<Container> optionalContainer = KubeUtils.findAppContainerOf(deployment);
                        return optionalContainer.map(
                                        container -> KubernetesDeploymentAppVersionVO.DeploymentImage.builder()
                                                .name(deployment.getMetadata()
                                                              .getName())
                                                .image(container.getImage())
                                                .build())
                                .orElse(null);
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();
    }

    @SuppressWarnings("unchecked")
    private EdsInstanceProviderHolder<EdsConfigs.Kubernetes, Deployment> createHolder(int instanceId) {
        return (EdsInstanceProviderHolder<EdsConfigs.Kubernetes, Deployment>) edsProviderHolderFactory.createHolder(
                instanceId, EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name());
    }

}
