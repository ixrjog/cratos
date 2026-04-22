package com.baiyi.cratos.eds.kubernetes;

import com.baiyi.cratos.common.table.PrettyTable;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.eds.BaseEdsTest;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsProviderHolderFactory;
import com.baiyi.cratos.eds.core.support.EdsInstanceAssetProvider;
import com.baiyi.cratos.eds.kubernetes.repo.template.KubernetesDeploymentRepo;
import com.baiyi.cratos.eds.kubernetes.util.KubeUtils;
import com.baiyi.cratos.service.EdsAssetIndexService;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.util.KubernetesResourceUtils;
import com.google.api.client.util.Lists;
import com.google.common.collect.Maps;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.Quantity;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/3/12 17:39
 * &#064;Version 1.0
 */
@SuppressWarnings("unchecked")
@Slf4j
public class EdsKubernetes2Test extends BaseEdsTest<EdsConfigs.Kubernetes> {

    // BD-DHK-CCE-PROD 147
    // BD-JSR-CCE-PROD 129
    // PK-ISB-CCE-PROD 136
    // PK-LHR-CCE-PROD 140

    @Resource
    private EdsAssetService edsAssetService;

    @Resource
    private EdsProviderHolderFactory edsProviderHolderFactory;

    @Resource
    private KubernetesResourceUtils kubernetesResourceUtils;

    @Resource
    private KubernetesDeploymentRepo kubernetesDeploymentRepo;
    @Autowired
    private EdsAssetIndexService edsAssetIndexService;



    @Test
    void test1() {
        int instanceId = 140;
        System.out.println(kubernetesResourceUtils.printKubernetesResourceTable(instanceId));
    }

    @Test
    void test2() {
        int instanceId = 129;
        List<EdsAsset> assets = edsAssetService.queryInstanceAssets(
                instanceId, EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name());
        EdsInstanceProviderHolder<EdsConfigs.Kubernetes, Deployment> holder = (EdsInstanceProviderHolder<EdsConfigs.Kubernetes, Deployment>) edsProviderHolderFactory.createHolder(
                instanceId, EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name());
        EdsInstanceAssetProvider<EdsConfigs.Kubernetes, Deployment> provider = holder.getProvider();
        for (EdsAsset asset : assets) {
            try {
                Deployment deployment = provider.loadAsset(asset.getOriginalModel());
                if (!deployment.getMetadata()
                        .getNamespace()
                        .equals("prod")) {
                    continue;
                }
                KubeUtils.findAppContainerOf(deployment)
                        .ifPresent(container -> {
                            System.out.println(container.getName());
                            // 修改 requests/limits
                            container.getResources()
                                    .getLimits()
                                    .put("cpu", new Quantity("1"));
                            container.getResources()
                                    .getLimits()
                                    .put("memory", new Quantity("3Gi"));
                            container.getResources()
                                    .getRequests()
                                    .put("cpu", new Quantity("500m"));
                            container.getResources()
                                    .getRequests()
                                    .put("memory", new Quantity("2Gi"));
                            // 修改JVM 内存参数
                            container.getEnv()
                                    .stream()
                                    .filter(env -> env.getName()
                                            .equals("JAVA_OPTS"))
                                    .findFirst()
                                    .ifPresent(env -> {
                                        // 删除所有 -Xms/-Xmx/-Xmn，统一替换为 -Xms2G -Xmx2G
                                        String value = env.getValue()
                                                .replaceAll("-Xm[sxn]\\S*", "")
                                                .trim()
                                                .replaceAll("\\s+", " ");
                                        env.setValue("-Xms2G -Xmx2G " + value);
                                        System.out.println(env.getValue());
                                    });
                            kubernetesDeploymentRepo.update(
                                    holder.getInstance()
                                            .getConfig(), deployment
                            );
                        });
            } catch (Exception e) {
                log.error("Failed to process deployment {}: {}", asset.getName(), e.getMessage());
            }
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AppDeploymentAssets {
        @Builder.Default
        private List<EdsAsset> dcAssets = Lists.newArrayList();
        @Builder.Default
        private List<EdsAsset> drAssets = Lists.newArrayList();
    }

    // DC DR 镜像同步
    @Test
    void test3() {
        int dcInstanceId = 136;
        int drInstanceId = 140;
        List<EdsAsset> dcAssets = edsAssetService.queryInstanceAssets(
                dcInstanceId, EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name());
        List<EdsAsset> drAssets = edsAssetService.queryInstanceAssets(
                drInstanceId, EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name());

        Map<String, AppDeploymentAssets> appMap = Maps.newHashMap();
        // DC
        dcAssets.forEach(a -> {
            EdsAssetIndex appNameIndex = edsAssetIndexService.getByAssetIdAndName(a.getId(), "appName");
            if (appNameIndex == null) {
                return;
            }
            EdsAssetIndex namespaceIndex = edsAssetIndexService.getByAssetIdAndName(a.getId(), "namespace");
            if (namespaceIndex == null || !"prod".equals(namespaceIndex.getValue())) {
                return;
            }
            if (appMap.containsKey(appNameIndex.getValue())) {
                appMap.get(appNameIndex.getValue())
                        .getDcAssets()
                        .add(a);
            } else {
                AppDeploymentAssets appDeploymentAssets = AppDeploymentAssets.builder()
                        .build();
                appDeploymentAssets.getDcAssets()
                        .add(a);
                appMap.put(appNameIndex.getValue(), appDeploymentAssets);
            }
        });
        // DR
        drAssets.forEach(a -> {
            EdsAssetIndex appNameIndex = edsAssetIndexService.getByAssetIdAndName(a.getId(), "appName");
            if (appNameIndex == null) {
                return;
            }
            EdsAssetIndex namespaceIndex = edsAssetIndexService.getByAssetIdAndName(a.getId(), "namespace");
            if (namespaceIndex == null || !"prod".equals(namespaceIndex.getValue())) {
                return;
            }
            if (appMap.containsKey(appNameIndex.getValue())) {
                appMap.get(appNameIndex.getValue())
                        .getDrAssets()
                        .add(a);
            } else {
                AppDeploymentAssets appDeploymentAssets = AppDeploymentAssets.builder()
                        .build();
                appDeploymentAssets.getDrAssets()
                        .add(a);
                appMap.put(appNameIndex.getValue(), appDeploymentAssets);
            }
        });
        EdsInstanceProviderHolder<EdsConfigs.Kubernetes, Deployment> dcHolder = (EdsInstanceProviderHolder<EdsConfigs.Kubernetes, Deployment>) edsProviderHolderFactory.createHolder(
                dcInstanceId, EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name());

        EdsInstanceProviderHolder<EdsConfigs.Kubernetes, Deployment> drHolder = (EdsInstanceProviderHolder<EdsConfigs.Kubernetes, Deployment>) edsProviderHolderFactory.createHolder(
                drInstanceId, EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name());

        PrettyTable pt = PrettyTable.fieldNames("APP", "DC PK-ISB-CCE-PROD Image", "DC PK-LHR-CCE-PROD Image");


        appMap.forEach((k, v) -> {
            String dcImages = v.getDcAssets()
                    .stream()
                    .map(dcAsset -> {
                        Deployment deployment = dcHolder.getProvider()
                                .loadAsset(dcAsset.getOriginalModel());
                        Optional<Container> optionalContainer = KubeUtils.findAppContainerOf(deployment);
                        if (optionalContainer.isPresent()) {
                            Container container = optionalContainer.get();
                            return k + ":" + container.getImage();
                        } else {
                            return k + ":" + "Not found image";
                        }
                    })
                    .collect(Collectors.joining(","));
            String drImages = v.getDrAssets()
                    .stream()
                    .map(dcAsset -> {
                        Deployment deployment = drHolder.getProvider()
                                .loadAsset(dcAsset.getOriginalModel());
                        Optional<Container> optionalContainer = KubeUtils.findAppContainerOf(deployment);
                        if (optionalContainer.isPresent()) {
                            Container container = optionalContainer.get();
                            return k + ":" + container.getImage();
                        } else {
                            return k + ":" + "Not found image";
                        }
                    })
                    .collect(Collectors.joining(","));
            pt.addRow(k, dcImages, drImages);
        });

        System.out.println(pt);

      //  handle(appMap, dcHolder, drHolder);
    }


    private void handle(Map<String, AppDeploymentAssets> appMap,
                        EdsInstanceProviderHolder<EdsConfigs.Kubernetes, Deployment> dcHolder,
                        EdsInstanceProviderHolder<EdsConfigs.Kubernetes, Deployment> drHolder) {
        appMap.forEach((k, v) -> {
            if (v.getDcAssets()
                    .size() == 1 && v.getDrAssets()
                    .size() == 1) {
                // DC
                EdsAsset dcAsset = v.getDcAssets()
                        .getFirst();
                Deployment dcDeployment = dcHolder.getProvider()
                        .loadAsset(dcAsset.getOriginalModel());
                Optional<Container> optionalDcContainer = KubeUtils.findAppContainerOf(dcDeployment);
                String dcImage = optionalDcContainer.get()
                        .getImage();
                // DR
                EdsAsset drAsset = v.getDrAssets()
                        .getFirst();
                Deployment drDeployment = drHolder.getProvider()
                        .loadAsset(drAsset.getOriginalModel());
                Optional<Container> optionalDrContainer = KubeUtils.findAppContainerOf(drDeployment);
                Container drContainer = optionalDrContainer.get();
                String drImage = drContainer.getImage();
                if (!dcImage.equals(drImage)) {
                    // DC DR 镜像不同，修改镜像
                    drContainer.setImage(dcImage);
                    kubernetesDeploymentRepo.update(
                            drHolder.getInstance()
                                    .getConfig(), drDeployment
                    );
                }
            }

        });
    }


}
