package com.baiyi.cratos.eds.zabbix.sender;

import com.baiyi.cratos.common.builder.SimpleMapBuilder;
import com.baiyi.cratos.common.enums.SysTagKeys;
import com.baiyi.cratos.common.util.beetl.BeetlUtil;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsConfig;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.generator.NotificationTemplate;
import com.baiyi.cratos.eds.core.EdsInstanceQueryHelper;
import com.baiyi.cratos.eds.core.config.model.EdsDingtalkConfigModel;
import com.baiyi.cratos.eds.core.config.model.EdsZabbixConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.dingtalk.model.DingtalkRobotModel;
import com.baiyi.cratos.eds.dingtalk.service.DingtalkService;
import com.baiyi.cratos.eds.zabbix.result.ZbxEventResult;
import com.baiyi.cratos.eds.zabbix.util.ZbxAlertUtils;
import com.baiyi.cratos.service.EdsConfigService;
import com.baiyi.cratos.service.NotificationTemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.List;

import static com.baiyi.cratos.common.enums.NotificationTemplateKeys.ZBX_ALERT_NOTIFICATION;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/11/6 17:17
 * &#064;Version 1.0
 */
@SuppressWarnings("ALL")
@Slf4j
@Component
@RequiredArgsConstructor
public class AlertNotificationSender {

    private final EdsInstanceQueryHelper edsInstanceQueryHelper;
    private final EdsConfigService edsConfigService;
    private final DingtalkService dingtalkService;
    private final NotificationTemplateService notificationTemplateService;
    private final EdsInstanceProviderHolderBuilder holderBuilder;

    @Value("${cratos.language:en-us}")
    protected String language;

    public void sendAlertNotice(EdsAsset asset) {
        EdsInstanceProviderHolder<EdsZabbixConfigModel.Zabbix, ZbxEventResult.Event> zbxEventHolder = (EdsInstanceProviderHolder<EdsZabbixConfigModel.Zabbix, ZbxEventResult.Event>) holderBuilder.newHolder(
                asset.getInstanceId(), EdsAssetTypeEnum.ZBX_EVENT.name());
        EdsZabbixConfigModel.Zabbix zbx = zbxEventHolder.getInstance()
                .getConfig();
        if (ZbxAlertUtils.matchRule(zbx, asset.getName())) {
            // 静默告警
            return;
        }
        List<EdsInstance> edsInstanceList = queryDingtalkRobotInstances();
        if (CollectionUtils.isEmpty(edsInstanceList)) {
            log.warn("No available robots to send alert notifications.");
            return;
        }
        try {
            DingtalkRobotModel.Msg message = getMsg(asset);
            List<? extends EdsInstanceProviderHolder<EdsDingtalkConfigModel.Robot, DingtalkRobotModel.Msg>> dingtalkRobotHolders = (List<? extends EdsInstanceProviderHolder<EdsDingtalkConfigModel.Robot, DingtalkRobotModel.Msg>>) edsInstanceQueryHelper.buildHolder(
                    edsInstanceList, EdsAssetTypeEnum.DINGTALK_ROBOT_MSG.name());
            dingtalkRobotHolders.forEach(providerHolder -> {
                EdsConfig edsConfig = edsConfigService.getById(providerHolder.getInstance()
                                                                       .getEdsInstance()
                                                                       .getConfigId());
                EdsDingtalkConfigModel.Robot robot = providerHolder.getProvider()
                        .configLoadAs(edsConfig);
                dingtalkService.send(robot.getToken(), message);
                providerHolder.importAsset(message);
            });
        } catch (IOException ioException) {
            log.error(ioException.getMessage(), ioException);
        }
    }

    private List<EdsInstance> queryDingtalkRobotInstances() {
        return edsInstanceQueryHelper.queryValidEdsInstance(
                EdsInstanceTypeEnum.DINGTALK_ROBOT, SysTagKeys.ALERT_NOTIFICATION.getKey());
    }

    protected DingtalkRobotModel.Msg getMsg(EdsAsset asset) throws IOException {
        NotificationTemplate notificationTemplate = getNotificationTemplate();
        String msg = BeetlUtil.renderTemplate(
                notificationTemplate.getContent(), SimpleMapBuilder.newBuilder()
                        .put("eventId", asset.getAssetId())
                        .put("message", asset.getAssetKey())
                        .build()
        );
        return DingtalkRobotModel.loadAs(msg);
    }

    private NotificationTemplate getNotificationTemplate() {
        NotificationTemplate query = NotificationTemplate.builder()
                .notificationTemplateKey(ZBX_ALERT_NOTIFICATION.name())
                .lang(language)
                .build();
        return notificationTemplateService.getByUniqueKey(query);
    }




}
