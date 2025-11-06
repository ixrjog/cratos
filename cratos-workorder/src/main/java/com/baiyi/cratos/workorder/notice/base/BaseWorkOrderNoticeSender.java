package com.baiyi.cratos.workorder.notice.base;

import com.baiyi.cratos.common.enums.NotificationTemplateKeys;
import com.baiyi.cratos.common.util.beetl.BeetlUtil;
import com.baiyi.cratos.domain.generator.NotificationTemplate;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.eds.core.facade.EdsDingtalkMessageFacade;
import com.baiyi.cratos.service.NotificationTemplateService;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.util.LanguageUtils;
import com.baiyi.cratos.workorder.facade.TicketWorkflowFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/1 14:05
 * &#064;Version 1.0
 */
@Slf4j
@RequiredArgsConstructor
public abstract class BaseWorkOrderNoticeSender {

    protected final WorkOrderTicketEntryService workOrderTicketEntryService;
    protected final UserService userService;
    protected final TicketWorkflowFacade ticketWorkflowFacade;
    protected final EdsDingtalkMessageFacade edsDingtalkMessageFacade;
    protected final LanguageUtils languageUtils;
    private final NotificationTemplateService notificationTemplateService;

    private NotificationTemplate getNotificationTemplate(String notificationTemplateKey, User user) {
        NotificationTemplate query = NotificationTemplate.builder()
                .notificationTemplateKey(notificationTemplateKey)
                .lang(languageUtils.getLanguageOf(user))
                .build();
        return notificationTemplateService.getByUniqueKey(query);
    }

    protected void sendMsgToUsername(String username, String notificationTemplateKey, Map<String, Object> dict) {
        User user = userService.getByUsername(username);
        sendMsgToUser(user, notificationTemplateKey, dict);
    }

    protected void sendMsgToUser(User sendToUser, NotificationTemplateKeys notificationTemplateKey, Map<String, Object> dict) {
        this.sendMsgToUser(sendToUser, notificationTemplateKey.name(), dict);
    }

    protected void sendMsgToUser(User sendToUser, String notificationTemplateKey, Map<String, Object> dict) {
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
