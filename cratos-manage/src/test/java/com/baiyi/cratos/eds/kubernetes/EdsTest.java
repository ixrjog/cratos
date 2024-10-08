package com.baiyi.cratos.eds.kubernetes;

import com.baiyi.cratos.common.util.StringFormatter;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.eds.BaseEdsTest;
import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.service.EdsAssetIndexService;
import com.baiyi.cratos.service.EdsAssetService;
import com.google.api.client.util.Maps;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/10/8 17:24
 * &#064;Version 1.0
 */
public class EdsTest extends BaseEdsTest<EdsKubernetesConfigModel.Kubernetes> {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ApplicationView {
        private String name;
        @Builder.Default
        private Map<String, Integer> replicasMap = Maps.newHashMap();
    }

    @Resource
    private EdsAssetService edsAssetService;

    @Resource
    private EdsAssetIndexService edsAssetIndexService;

    @Test
    void test() {
        Map<String, ApplicationView> viewMap = Maps.newHashMap();
        // EKS-TEST
        makeDeployments(96, "ci", viewMap);
        makeDeployments(96, "test", viewMap);
        // EKS-SIT | gray
        makeDeployments(106, "gray", viewMap);
        // EKS-PROD
        makeDeployments(105, "prod", viewMap);
        viewMap.keySet()
                .forEach(k -> {
                    Integer dev = viewMap.get(k)
                            .getReplicasMap()
                            .getOrDefault("ci", 0);
                    Integer daily = viewMap.get(k)
                            .getReplicasMap()
                            .getOrDefault("daily", 0);
                    Integer gray = viewMap.get(k)
                            .getReplicasMap()
                            .getOrDefault("gray", 0);
                    Integer prod = viewMap.get(k)
                            .getReplicasMap()
                            .getOrDefault("prod", 0);
                    String s = StringFormatter.arrayFormat("{} : dev={} daily={} gray={} prod={}", k, dev, daily, gray,
                            prod);
                    System.out.println(s);
                });
    }

    private void makeDeployments(int instanceId, String namespace, Map<String, ApplicationView> viewMap) {
        List<EdsAsset> assets = edsAssetService.queryInstanceAssets(instanceId,
                EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name());

        assets.forEach(asset -> {
            EdsAssetIndex appNameUniqueKey = EdsAssetIndex.builder()
                    .instanceId(instanceId)
                    .assetId(asset.getId())
                    .name("appName")
                    .build();
            EdsAssetIndex appNameIndex = edsAssetIndexService.getByUniqueKey(appNameUniqueKey);
            if (appNameIndex != null) {
                EdsAssetIndex namespaceUniqueKey = EdsAssetIndex.builder()
                        .instanceId(instanceId)
                        .assetId(asset.getId())
                        .name("namespace")
                        .build();
                EdsAssetIndex namespaceIndex = edsAssetIndexService.getByUniqueKey(namespaceUniqueKey);
                if (namespaceIndex != null) {

                    EdsAssetIndex replicasUniqueKey = EdsAssetIndex.builder()
                            .instanceId(instanceId)
                            .assetId(asset.getId())
                            .name("replicas")
                            .build();
                    EdsAssetIndex replicasIndex = edsAssetIndexService.getByUniqueKey(replicasUniqueKey);
                    int replicas = replicasIndex != null ? Integer.parseInt(replicasIndex.getValue()) : 0;

                    if (viewMap.containsKey(appNameIndex.getValue())) {
                        viewMap.get(appNameIndex.getValue())
                                .getReplicasMap()
                                .put(namespace, replicas);
                    } else {
                        ApplicationView view = ApplicationView.builder()
                                .name(appNameIndex.getValue())
                                .build();
                        view.getReplicasMap()
                                .put(namespace, replicas);
                        viewMap.put(appNameIndex.getValue(), view);
                    }
                }

            }


        });


    }

}
