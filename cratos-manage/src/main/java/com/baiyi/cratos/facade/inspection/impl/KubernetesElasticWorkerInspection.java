package com.baiyi.cratos.facade.inspection.impl;

import com.baiyi.cratos.common.builder.SimpleMapBuilder;
import com.baiyi.cratos.common.util.BeetlUtil;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.generator.NotificationTemplate;
import com.baiyi.cratos.eds.EdsInstanceHelper;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.dingtalk.service.DingtalkService;
import com.baiyi.cratos.facade.inspection.base.BaseInspection;
import com.baiyi.cratos.facade.inspection.model.KubernetesElasticWorkerModel;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.EdsConfigService;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.service.NotificationTemplateService;
import com.google.api.client.util.Maps;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/9 09:51
 * &#064;Version 1.0
 */
@Component
public class KubernetesElasticWorkerInspection extends BaseInspection {

    private static final String WORKER_NAME_PREFIX = "ack-elastic-worker";
    private static final String KUBERNETES_ELASTIC_WORKER_INSPECTION_NOTIFICATION = "KUBERNETES_ELASTIC_WORKER_INSPECTION_NOTIFICATION";
    private static final String WORKERS_FIELD = "workers";

    private final EdsAssetService edsAssetService;
    private final EdsInstanceService edsInstanceService;

    public KubernetesElasticWorkerInspection(NotificationTemplateService notificationTemplateService,
                                             DingtalkService dingtalkService, EdsInstanceHelper edsInstanceHelper,
                                             EdsConfigService edsConfigService, EdsInstanceService edsInstanceService,
                                             EdsAssetService edsAssetService) {
        super(notificationTemplateService, dingtalkService, edsInstanceHelper, edsConfigService);
        this.edsInstanceService = edsInstanceService;
        this.edsAssetService = edsAssetService;
    }

    @Override
    protected String getMsg() throws IOException {
        Map<Integer, String> instanceMap = Maps.newHashMap();
        // 名称前缀查询所有弹性节点
        List<KubernetesElasticWorkerModel.Worker> workers = edsAssetService.queryByTypeAndName(
                        EdsAssetTypeEnum.KUBERNETES_NODE.name(), WORKER_NAME_PREFIX, true)
                .stream()
                .map(e -> KubernetesElasticWorkerModel.Worker.builder()
                        .instanceName(getInstanceName(e.getInstanceId(), instanceMap))
                        .name(e.getName())
                        .build())
                .toList();
        NotificationTemplate notificationTemplate = getNotificationTemplate(
                KUBERNETES_ELASTIC_WORKER_INSPECTION_NOTIFICATION);
        return BeetlUtil.renderTemplate(notificationTemplate.getContent(), SimpleMapBuilder.newBuilder()
                .put(WORKERS_FIELD, workers)
                .build());
    }

    private String getInstanceName(int instanceId, Map<Integer, String> instanceMap) {
        if (instanceMap.containsKey(instanceId)) {
            return instanceMap.get(instanceId);
        }
        EdsInstance edsInstance = edsInstanceService.getById(instanceId);
        String instanceName = edsInstance == null ? "Instance does not exist!" : edsInstance.getInstanceName();
        instanceMap.put(instanceId, instanceName);
        return instanceName;
    }

}
