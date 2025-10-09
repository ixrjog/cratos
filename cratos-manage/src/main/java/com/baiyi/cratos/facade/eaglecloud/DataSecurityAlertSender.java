package com.baiyi.cratos.facade.eaglecloud;

import com.baiyi.cratos.common.builder.SimpleMapBuilder;
import com.baiyi.cratos.common.enums.NotificationTemplateKeys;
import com.baiyi.cratos.common.enums.SysTagKeys;
import com.baiyi.cratos.common.util.beetl.BeetlUtil;
import com.baiyi.cratos.domain.SimpleBusiness;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.facade.BusinessTagFacade;
import com.baiyi.cratos.domain.generator.*;
import com.baiyi.cratos.domain.param.http.event.EagleCloudEventParam;
import com.baiyi.cratos.domain.util.StringFormatter;
import com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants;
import com.baiyi.cratos.eds.core.facade.EdsDingtalkMessageFacade;
import com.baiyi.cratos.eds.eaglecloud.sase.model.EagleCloudModel;
import com.baiyi.cratos.service.BusinessTagService;
import com.baiyi.cratos.service.EdsAssetIndexService;
import com.baiyi.cratos.service.NotificationTemplateService;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.util.LanguageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/9/30 13:58
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataSecurityAlertSender {

    private final UserService userService;
    private final EdsDingtalkMessageFacade edsDingtalkMessageFacade;
    private final LanguageUtils languageUtils;
    private final NotificationTemplateService notificationTemplateService;
    private final BusinessTagService businessTagService;
    private final BusinessTagFacade businessTagFacade;
    private final EdsAssetIndexService edsAssetIndexService;

    public EagleCloudModel.AlertRecord sendMsgToManager(EdsAsset dingtalkManager, EagleCloudEventParam.Alert alert) {
        EagleCloudEventParam.Content content = alert.getContent();
        Map<String, Object> dict = SimpleMapBuilder.newBuilder()
                .put("action", content.getAction())
                .put("eventId", content.getEventId())
                .put("threshold", content.getThreshold())
                .put("entityName", content.getEntityName())
                .put("eventName", content.getEventName())
                .put("timeStr", content.getTimeStr())
                .put("actionUrl", alert.getActionUrl())
                .build();
        List<User> managerUsers = queryManagerUsers(dingtalkManager);
        if (CollectionUtils.isEmpty(managerUsers)) {
            return null;
        }
        EagleCloudModel.AlertRecord alertRecord = EagleCloudModel.AlertRecord.builder()
                .eventId(content.getEventId())
                .name(StringFormatter.arrayFormat("{} -> {}", content.getEntityName(), content.getThreshold()))
                .description(content.getThreshold())
                .content(dict)
                .build();
        managerUsers.forEach(managerUser -> {
            sendMsgToUser(managerUser, dict);
            alertRecord.getManagers()
                    .add(EagleCloudModel.Manager.builder()
                            .username(managerUser.getUsername())
                            .build());
        });
        return alertRecord;
    }

    private List<User> queryManagerUsers(EdsAsset dingtalkManager) {
        // 查询Username标签
        BusinessTag businessTag = businessTagFacade.getBusinessTag(SimpleBusiness.builder()
                .businessId(dingtalkManager.getId())
                .businessType(BusinessTypeEnum.EDS_ASSET.name())
                .build(), SysTagKeys.USERNAME.getKey());
        if (businessTag != null) {
            return List.of(userService.getByUsername(businessTag.getTagValue()));
        }
        // 查询邮箱
        EdsAssetIndex userMailIndex = edsAssetIndexService.getByAssetIdAndName(dingtalkManager.getId(),
                EdsAssetIndexConstants.USER_MAIL);
        if (userMailIndex != null) {
            List<User> users = userService.queryByEmail(userMailIndex.getValue());
            if (!CollectionUtils.isEmpty(users)) {
                return users;
            }
        }
        // 查询手机号
        EdsAssetIndex userMobileIndex = edsAssetIndexService.getByAssetIdAndName(dingtalkManager.getId(),
                EdsAssetIndexConstants.DINGTALK_USER_MOBILE);
        if (userMobileIndex != null) {
            List<User> users = userService.queryByMobilePhone(userMobileIndex.getValue());
            if (!CollectionUtils.isEmpty(users)) {
                return users;
            }
        }
        return List.of();
    }

    private void sendMsgToUser(User sendToUser, Map<String, Object> dict) {
        sendMsgToUser(sendToUser, NotificationTemplateKeys.DATA_SECURITY_ALERT_NOTICE.name(), dict);
    }

    private NotificationTemplate getNotificationTemplate(String notificationTemplateKey, User user) {
        NotificationTemplate query = NotificationTemplate.builder()
                .notificationTemplateKey(notificationTemplateKey)
                .lang(languageUtils.getLanguageOf(user))
                .build();
        return notificationTemplateService.getByUniqueKey(query);
    }

    private void sendMsgToUser(User sendToUser, String notificationTemplateKey, Map<String, Object> dict) {
        if (sendToUser == null) {
            return;
        }
        try {
            NotificationTemplate notificationTemplate = getNotificationTemplate(notificationTemplateKey, sendToUser);
            String msg = BeetlUtil.renderTemplate(notificationTemplate.getContent(), dict);
            edsDingtalkMessageFacade.sendToDingtalkUser(sendToUser, notificationTemplate, msg);
        } catch (IOException ioException) {
            log.error("WorkOrder ticket send msg to user err: {}", ioException.getMessage());
        }
    }

}
