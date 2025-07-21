package com.baiyi.cratos.my;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.domain.generator.Application;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.eds.core.config.EdsKubernetesConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.kubernetes.repo.template.KubernetesDeploymentRepo;
import com.baiyi.cratos.service.ApplicationService;
import com.baiyi.cratos.service.EdsAssetIndexService;
import com.baiyi.cratos.service.EdsAssetService;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.PodTemplateSpec;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentSpec;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.ENV;
import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.KUBERNETES_APP_NAME;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/7/18 09:51
 * &#064;Version 1.0
 */
@Slf4j
public class ArmsTest extends BaseUnit {

    @Resource
    private EdsAssetService edsAssetService;

    @Resource
    private EdsAssetIndexService edsAssetIndexService;

    @Resource
    private ApplicationService applicationService;

    @Resource
    private EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder;
    @Resource
    private KubernetesDeploymentRepo kubernetesDeploymentRepo;

    @Test
    void test() {
        List<EdsAsset> armsApps = edsAssetService.queryInstanceAssets(93,
                EdsAssetTypeEnum.ALIYUN_ARMS_TRACE_APPS.name());
        List<Application> applications = applicationService.selectAll();
        Set<String> appSet = applications.stream()
                .map(Application::getName)
                .collect(Collectors.toSet());

        armsApps.forEach(armsApp -> {
            String env = armsApp.getName()
                    .substring(armsApp.getName()
                            .lastIndexOf('-') + 1);
            String appName = armsApp.getName()
                    .substring(0, armsApp.getName()
                            .lastIndexOf('-'));
            // 只处理daily
            if ("daily".equals(env) && !appSet.contains(appName)) {
                System.out.println(armsApp.getName());
            }
        });
    }

    @Test
    @SuppressWarnings("unchecked")
    void test2() {
        List<EdsAsset> deployments = edsAssetService.queryInstanceAssets(103,
                EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name());
        EdsInstanceProviderHolder<EdsKubernetesConfigModel.Kubernetes, Deployment> holder = (EdsInstanceProviderHolder<EdsKubernetesConfigModel.Kubernetes, Deployment>) edsInstanceProviderHolderBuilder.newHolder(
                103, EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name());

        for (EdsAsset deploymentAsset : deployments) {
            EdsAssetIndex appNameIndex = edsAssetIndexService.getByAssetIdAndName(deploymentAsset.getId(),
                    KUBERNETES_APP_NAME);
            if (Objects.isNull(appNameIndex)) {
                continue;
            }
            EdsAssetIndex envIndex = edsAssetIndexService.getByAssetIdAndName(deploymentAsset.getId(), ENV);
            if (Objects.isNull(envIndex)) {
                continue;
            }
            Deployment deployment = kubernetesDeploymentRepo.get(holder.getInstance()
                    .getEdsConfigModel(), "daily", deploymentAsset.getName());
            if (Objects.isNull(deployment)) {
                log.warn("Deployment not found: {}", deploymentAsset.getName());
                continue;
            }
            Map<String, String> labels = Optional.of(deployment)
                    .map(Deployment::getSpec)
                    .map(DeploymentSpec::getTemplate)
                    .map(PodTemplateSpec::getMetadata)
                    .map(ObjectMeta::getLabels)
                    .orElse(Map.of());
            if (labels.containsKey("armsPilotCreateAppName")) {
                String armsPilotCreateAppName = appNameIndex.getValue() + "-" + envIndex.getValue();
                String labelsArmsPilotCreateAppName = labels.get("armsPilotCreateAppName");
                if (!labelsArmsPilotCreateAppName.equals(armsPilotCreateAppName)) {
                    deployment.getSpec()
                            .getTemplate()
                            .getMetadata()
                            .getLabels()
                            .put("armsPilotCreateAppName", armsPilotCreateAppName);
                    log.info("armsPilotCreateAppName updated: {} -> {}", labelsArmsPilotCreateAppName,
                            armsPilotCreateAppName);
                    kubernetesDeploymentRepo.update(holder.getInstance()
                            .getEdsConfigModel(), deployment);
                }
            } else {
                log.warn("ArmsPilotCreateAppName not found: {}", deploymentAsset.getName());
            }
        }
    }

}
