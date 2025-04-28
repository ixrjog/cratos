package com.baiyi.cratos.facade.inspection.impl;

import com.baiyi.cratos.common.builder.SimpleMapBuilder;
import com.baiyi.cratos.common.util.beetl.BeetlUtil;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.domain.generator.NotificationTemplate;
import com.baiyi.cratos.domain.util.BeanCopierUtil;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.eds.core.EdsInstanceHelper;
import com.baiyi.cratos.eds.core.EdsInstanceProviderFactory;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.dingtalk.service.DingtalkService;
import com.baiyi.cratos.eds.kubernetes.util.KubeUtil;
import com.baiyi.cratos.facade.inspection.base.BaseInspection;
import com.baiyi.cratos.facade.inspection.model.DeploymentImageModel;
import com.baiyi.cratos.service.EdsAssetIndexService;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.EdsConfigService;
import com.baiyi.cratos.service.NotificationTemplateService;
import com.google.api.client.util.Lists;
import com.google.common.collect.Maps;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.KUBERNETES_REPLICAS;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/8/26 17:22
 * &#064;Version 1.0
 */
@Component
public class AppDeploymentImageInspection extends BaseInspection {

    public static final String APPLICATION_DEPLOYMENT_IMAGE_COMPLIANCE_INSPECTION_NOTIFICATION = "APPLICATION_DEPLOYMENT_IMAGE_COMPLIANCE_INSPECTION_NOTIFICATION";

    private final EdsAssetService edsAssetService;
    private final EdsAssetIndexService edsAssetIndexService;

    private static final int ACK_PROD_INSTANCE_ID = 101;
    private static final int EKS_PROD_INSTANCE_ID = 105;
    private static final String APPS_FIELD = "apps";
    private static final String[] FILTER_LIST = {"-1", "-2", "-3", "-4"};

    public AppDeploymentImageInspection(NotificationTemplateService notificationTemplateService,
                                        DingtalkService dingtalkService, EdsInstanceHelper edsInstanceHelper,
                                        EdsConfigService edsConfigService, EdsAssetService edsAssetService,
                                        EdsAssetIndexService edsAssetIndexService) {
        super(notificationTemplateService, dingtalkService, edsInstanceHelper, edsConfigService);
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
        return BeetlUtil.renderTemplate(notificationTemplate.getContent(), SimpleMapBuilder.newBuilder()
                .put(APPS_FIELD, inspections)
                .build());
    }

    private Map<String, List<DeploymentImageModel.DeploymentImage>> getDeploymentImageMap(int instanceId) {
        List<EdsAsset> edsAssets = edsAssetService.queryInstanceAssets(instanceId,
                EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name());
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
            Deployment deployment = getAssetModel(BeanCopierUtil.copyProperties(e, EdsAssetVO.Asset.class));
            Optional<Container> optionalContainer = KubeUtil.findAppContainerOf(deployment);
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
        return EdsInstanceProviderFactory.produceModel(EdsInstanceTypeEnum.KUBERNETES.name(),
                EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name(), asset);
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
