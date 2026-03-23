package com.baiyi.cratos.eds.kubernetes;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.BaseEdsTest;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsProviderHolderFactory;
import com.baiyi.cratos.eds.core.support.EdsInstanceAssetProvider;
import com.baiyi.cratos.eds.kubernetes.repo.template.KubernetesDeploymentRepo;
import com.baiyi.cratos.eds.kubernetes.util.KubeUtils;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.util.KubernetesResourceUtils;
import io.fabric8.kubernetes.api.model.Quantity;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.List;

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
                                        String value = env.getValue().replaceAll("-Xm[sxn]\\S*", "").trim().replaceAll("\\s+", " ");
                                        env.setValue("-Xms2G -Xmx2G " + value);
                                        System.out.println(env.getValue());
                                    });
                            kubernetesDeploymentRepo.update(holder.getInstance()
                                                                    .getConfig(), deployment);
                        });
            } catch (Exception e) {
                log.error("Failed to process deployment {}: {}", asset.getName(), e.getMessage());
            }
        }

    }


}
