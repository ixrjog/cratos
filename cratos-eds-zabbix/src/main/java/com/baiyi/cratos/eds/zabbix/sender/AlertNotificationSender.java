package com.baiyi.cratos.eds.zabbix.sender;

import com.baiyi.cratos.common.builder.SimpleMapBuilder;
import com.baiyi.cratos.common.enums.SysTagKeys;
import com.baiyi.cratos.common.util.beetl.BeetlUtil;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsConfig;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.generator.NotificationTemplate;
import com.baiyi.cratos.eds.core.EdsInstanceHelper;
import com.baiyi.cratos.eds.core.config.EdsDingtalkConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.dingtalk.model.DingtalkRobotModel;
import com.baiyi.cratos.eds.dingtalk.service.DingtalkService;
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
@Slf4j
@Component
@RequiredArgsConstructor
public class AlertNotificationSender {

    private final EdsInstanceHelper edsInstanceHelper;
    private final EdsConfigService edsConfigService;
    private final DingtalkService dingtalkService;
    private final NotificationTemplateService notificationTemplateService;

    @Value("${cratos.language:en-us}")
    protected String language;

    public void sendAlertNotice(EdsAsset asset) {
        List<EdsInstance> edsInstanceList = edsInstanceHelper.queryValidEdsInstance(
                EdsInstanceTypeEnum.DINGTALK_ROBOT, SysTagKeys.ALERT_NOTIFICATION.getKey());
        if (CollectionUtils.isEmpty(edsInstanceList)) {
            log.warn("No available robots to send alert notifications.");
            return;
        }
        try {
            DingtalkRobotModel.Msg message = getMsg(asset);
            List<? extends EdsInstanceProviderHolder<EdsDingtalkConfigModel.Robot, DingtalkRobotModel.Msg>> holders = (List<? extends EdsInstanceProviderHolder<EdsDingtalkConfigModel.Robot, DingtalkRobotModel.Msg>>) edsInstanceHelper.buildHolder(
                    edsInstanceList, EdsAssetTypeEnum.DINGTALK_ROBOT_MSG.name());
            holders.forEach(providerHolder -> {
                EdsConfig edsConfig = edsConfigService.getById(providerHolder.getInstance()
                                                                       .getEdsInstance()
                                                                       .getConfigId());
                EdsDingtalkConfigModel.Robot robot = providerHolder.getProvider()
                        .produceConfig(edsConfig);
                dingtalkService.send(robot.getToken(), message);
                providerHolder.importAsset(message);
            });
        } catch (IOException ioException) {
            log.error(ioException.getMessage(), ioException);
        }
    }

    protected DingtalkRobotModel.Msg getMsg(EdsAsset asset) throws IOException {
        NotificationTemplate notificationTemplate = getNotificationTemplate();
        String msg = BeetlUtil.renderTemplate(
                notificationTemplate.getContent(), SimpleMapBuilder.newBuilder()
                        .put("message", asset.getName())
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
