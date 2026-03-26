package com.baiyi.cratos.facade.inspection.impl;

import com.baiyi.cratos.common.builder.SimpleMapBuilder;
import com.baiyi.cratos.common.util.PasswordGenerator;
import com.baiyi.cratos.common.util.beetl.BeetlUtil;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.domain.generator.NotificationTemplate;
import com.baiyi.cratos.domain.util.BeanCopierUtils;
import com.baiyi.cratos.domain.util.JSONUtils;
import com.baiyi.cratos.domain.util.StringFormatter;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.eds.core.EdsInstanceProviderFactory;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.util.SreBridgeUtils;
import com.baiyi.cratos.eds.core.util.SreEventFormatter;
import com.baiyi.cratos.eds.kubernetes.util.KubeUtils;
import com.baiyi.cratos.facade.inspection.base.BaseInspectionTask;
import com.baiyi.cratos.facade.inspection.context.InspectionTaskContext;
import com.baiyi.cratos.facade.inspection.model.DeploymentImageModel;
import com.baiyi.cratos.service.EdsAssetIndexService;
import com.baiyi.cratos.service.EdsAssetService;
import com.google.api.client.util.Lists;
import com.google.common.collect.Maps;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.baiyi.cratos.common.enums.NotificationTemplateKeys.APPLICATION_DEPLOYMENT_IMAGE_COMPLIANCE_INSPECTION_NOTIFICATION;
import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.KUBERNETES_REPLICAS;
import static com.baiyi.cratos.eds.core.util.SreEventFormatter.EVENT_ID;
import static java.util.Map.entry;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/8/26 17:22
 * &#064;Version 1.0
 */
@Slf4j
@Component
public class AppDeploymentImageInspectionTask extends BaseInspectionTask {

    private final EdsAssetService edsAssetService;
    private final EdsAssetIndexService edsAssetIndexService;

    private static final int ACK_PROD_INSTANCE_ID = 101;
    private static final int EKS_PROD_INSTANCE_ID = 105;
    private static final String APPS_FIELD = "apps";
    private static final String[] FILTER_LIST = {"-1", "-2", "-3", "-4"};

    public AppDeploymentImageInspectionTask(InspectionTaskContext context, EdsAssetService edsAssetService,
                                            EdsAssetIndexService edsAssetIndexService) {
        super(context);
        this.edsAssetService = edsAssetService;
        this.edsAssetIndexService = edsAssetIndexService;
    }

    @Override
    protected String getMsg() throws IOException {
        NotificationTemplate notificationTemplate = getNotificationTemplate(
                APPLICATION_DEPLOYMENT_IMAGE_COMPLIANCE_INSPECTION_NOTIFICATION);
        Map<String, List<DeploymentImageModel.DeploymentImage>> deploymentImageMap = Maps.newHashMap();
        deploymentImageMap.putAll(getDeploymentImageMap(ACK_PROD_INSTANCE_ID));
        deploymentImageMap.putAll(getDeploymentImageMap(EKS_PROD_INSTANCE_ID));
        List<DeploymentImageModel.DeploymentImageInspection> inspections = Lists.newArrayList();
        deploymentImageMap.keySet()
                .forEach(appName -> {
                    DeploymentImageModel.DeploymentImageInspection inspection = DeploymentImageModel.DeploymentImageInspection.builder()
                            .appName(appName)
                            .deploymentImages(deploymentImageMap.get(appName))
                            .build();
                    if (!inspection.isConsistency()) {
                        inspections.add(inspection);
                    }
                });
        inspections.forEach(this::publish);
        return BeetlUtil.renderTemplate(
                notificationTemplate.getContent(), SimpleMapBuilder.newBuilder()
                        .put(APPS_FIELD, inspections)
                        .build()
        );
    }

    private void publish(DeploymentImageModel.DeploymentImageInspection imageInspection) {
        try {
            String env = "prod";
            Map<String, String> ext = Map.of(EVENT_ID, PasswordGenerator.generateNo());
            Map<String, String> targetContent = Map.ofEntries(
                    entry("appName", imageInspection.getAppName()),
                    entry("deployments", JSONUtils.writeValueAsString(imageInspection.getDeploymentImages()))
            );
            com.baiyi.cratos.domain.model.SreBridgeModel.Event event = com.baiyi.cratos.domain.model.SreBridgeModel.Event.builder()
                    .operator(OPERATOR)
                    .action(SreEventFormatter.Action.INSPECT_IMAGE.getValue())
                    .description(StringFormatter.arrayFormat(
                            "Inspection detected version inconsistency across groups in application {}",
                            imageInspection.getAppName()
                    ))
                    .target(imageInspection.getAppName())
                    .targetContent(SreEventFormatter.mapToJson(targetContent))
                    .affection("")
                    .severity("low")
                    .status("executed")
                    .type(SreEventFormatter.Type.INSPECTION.getValue())
                    .env(env)
                    .ext(ext)
                    .build();
            SreBridgeUtils.publish(event);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private Map<String, List<DeploymentImageModel.DeploymentImage>> getDeploymentImageMap(int instanceId) {
        List<EdsAsset> edsAssets = edsAssetService.queryInstanceAssets(
                instanceId, EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name());
        if (CollectionUtils.isEmpty(edsAssets)) {
            return Maps.newHashMap();
        }
        List<EdsAsset> assets = filter(edsAssets);
        if (CollectionUtils.isEmpty(assets)) {
            return Maps.newHashMap();
        }
        Map<String, List<DeploymentImageModel.DeploymentImage>> deploymentImageMap = Maps.newHashMap();
        assets.forEach(e -> {
            EdsAssetIndex appNameUniqueKey = EdsAssetIndex.builder()
                    .instanceId(e.getInstanceId())
                    .assetId(e.getId())
                    .name("appName")
                    .build();
            EdsAssetIndex appNameIndex = edsAssetIndexService.getByUniqueKey(appNameUniqueKey);
            if (appNameIndex == null) {
                return;
            }
            final String appName = appNameIndex.getValue();
            Deployment deployment = getAssetModel(BeanCopierUtils.copyProperties(e, EdsAssetVO.Asset.class));
            Optional<Container> optionalContainer = KubeUtils.findAppContainerOf(deployment);
            if (optionalContainer.isPresent()) {
                DeploymentImageModel.DeploymentImage image = DeploymentImageModel.DeploymentImage.builder()
                        .appName(appName)
                        .deploymentName(e.getName())
                        .image(optionalContainer.get()
                                       .getImage())
                        .build();
                if (deploymentImageMap.containsKey(appName)) {
                    deploymentImageMap.get(appName)
                            .add(image);
                } else {
                    List<DeploymentImageModel.DeploymentImage> images = Lists.newArrayList();
                    images.add(image);
                    deploymentImageMap.put(appName, images);
                }
            }
        });
        return deploymentImageMap;
    }

    private Deployment getAssetModel(EdsAssetVO.Asset asset) {
        return EdsInstanceProviderFactory.produceModel(
                EdsInstanceTypeEnum.KUBERNETES.name(), EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name(), asset);
    }

    private List<EdsAsset> filter(List<EdsAsset> edsAssets) {
        return edsAssets.stream()
                // 过滤 canary
                .filter(e -> !e.getName()
                        .endsWith("-canary"))
                // 过滤非分组
                .filter(e -> Arrays.stream(FILTER_LIST)
                        .anyMatch(s -> e.getName()
                                .endsWith(s)))
                // 过滤副本为0
                .filter(e -> {
                    EdsAssetIndex uniqueKey = EdsAssetIndex.builder()
                            .instanceId(e.getInstanceId())
                            .assetId(e.getId())
                            .name(KUBERNETES_REPLICAS)
                            .build();
                    EdsAssetIndex index = edsAssetIndexService.getByUniqueKey(uniqueKey);
                    if (index == null) {
                        return false;
                    }
                    return !"0".equals(index.getValue());
                })
                .toList();
    }

}
