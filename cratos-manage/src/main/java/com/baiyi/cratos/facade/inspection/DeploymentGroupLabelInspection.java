package com.baiyi.cratos.facade.inspection;

import com.baiyi.cratos.common.builder.SimpleMapBuilder;
import com.baiyi.cratos.common.util.BeetlUtil;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.generator.NotificationTemplate;
import com.baiyi.cratos.eds.EdsInstanceHelper;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.dingtalk.service.DingtalkRobotService;
import com.baiyi.cratos.facade.inspection.model.DeploymentInspectionModel;
import com.baiyi.cratos.service.*;
import com.google.api.client.util.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.baiyi.cratos.eds.kubernetes.provider.EdsKubernetesDeploymentAssetProvider.GROUP;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/6/11 上午11:08
 * &#064;Version 1.0
 */
@Slf4j
@Component
public class DeploymentGroupLabelInspection extends BaseInspection {

    public static final String DEPLOYMENT_GROUP_LABEL_INSPECTION_NOTIFICATION = "DEPLOYMENT_GROUP_LABEL_INSPECTION_NOTIFICATION";

    private final EdsInstanceService edsInstanceService;

    private final EdsAssetService edsAssetService;

    private final EdsAssetIndexService edsAssetIndexService;

    private static final int ACK_PROD_INSTANCE_ID = 101;

    private static final int EKS_PROD_INSTANCE_ID = 105;

    private static final String[] FILTER_LIST = {"prod:istio-ingressgateway", "prod:posp-nginx", "prod:config-server", "prod:config-server-nairabox", "prod:config-server-tecno"};

    private static final String DEPLOYMENTS_FIELD = "deployments";

    public DeploymentGroupLabelInspection(NotificationTemplateService notificationTemplateService,
                                          DingtalkRobotService dingtalkRobotService,
                                          EdsInstanceHelper edsInstanceHelper, EdsConfigService edsConfigService,
                                          EdsInstanceService edsInstanceService, EdsAssetService edsAssetService,
                                          EdsAssetIndexService edsAssetIndexService) {
        super(notificationTemplateService, dingtalkRobotService, edsInstanceHelper, edsConfigService);
        this.edsInstanceService = edsInstanceService;
        this.edsAssetService = edsAssetService;
        this.edsAssetIndexService = edsAssetIndexService;
    }

    @Override
    protected String getMsg() throws IOException {
        NotificationTemplate notificationTemplate = getNotificationTemplate(
                DEPLOYMENT_GROUP_LABEL_INSPECTION_NOTIFICATION);
        List<DeploymentInspectionModel.Deployment> deploymentList = Lists.newArrayList();
        deploymentList.addAll(getDeploymentList(ACK_PROD_INSTANCE_ID));
        deploymentList.addAll(getDeploymentList(EKS_PROD_INSTANCE_ID));
        return BeetlUtil.renderTemplate(notificationTemplate.getContent(), SimpleMapBuilder.newBuilder()
                .put(DEPLOYMENTS_FIELD, deploymentList)
                .build());
    }

    private List<DeploymentInspectionModel.Deployment> getDeploymentList(int instanceId) {
        List<EdsAsset> edsAssets = edsAssetService.queryInstanceAssets(instanceId,
                EdsAssetTypeEnum.KUBERNETES_DEPLOYMENT.name());
        if (CollectionUtils.isEmpty(edsAssets)) {
            return Lists.newArrayList();
        }
        EdsInstance edsInstance = edsInstanceService.getById(instanceId);
        List<DeploymentInspectionModel.Deployment> deploymentList = Lists.newArrayList();
        filter(edsAssets).forEach(e -> {
            EdsAssetIndex uniqueKey = EdsAssetIndex.builder()
                    .instanceId(e.getInstanceId())
                    .assetId(e.getId())
                    .name(GROUP)
                    .build();
            if (edsAssetIndexService.getByUniqueKey(uniqueKey) == null) {
                deploymentList.add(DeploymentInspectionModel.Deployment.builder()
                        .instanceName(edsInstance.getInstanceName())
                        .name(e.getAssetKey())
                        .build());
            }
        });
        return deploymentList;
    }

    private List<EdsAsset> filter(List<EdsAsset> edsAssets) {
        return edsAssets.stream()
                .filter(e -> e.getAssetKey()
                        .startsWith("prod:"))
                .filter(e -> Arrays.stream(FILTER_LIST)
                        .noneMatch(s -> e.getAssetKey()
                                .startsWith(s)))
                .toList();
    }

}
