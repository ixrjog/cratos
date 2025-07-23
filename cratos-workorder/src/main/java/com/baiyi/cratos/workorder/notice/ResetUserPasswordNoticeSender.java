package com.baiyi.cratos.workorder.notice;

import com.baiyi.cratos.common.builder.SimpleMapBuilder;
import com.baiyi.cratos.common.enums.NotificationTemplateKeys;
import com.baiyi.cratos.domain.generator.WorkOrder;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.eds.core.facade.EdsDingtalkMessageFacade;
import com.baiyi.cratos.service.NotificationTemplateService;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.util.LanguageUtils;
import com.baiyi.cratos.workorder.facade.TicketWorkflowFacade;
import com.baiyi.cratos.workorder.notice.base.BaseWorkOrderNoticeSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/7/23 14:54
 * &#064;Version 1.0
 */
@Slf4j
@Component
public class ResetUserPasswordNoticeSender extends BaseWorkOrderNoticeSender {

    public ResetUserPasswordNoticeSender(WorkOrderTicketEntryService workOrderTicketEntryService,
                                         UserService userService, TicketWorkflowFacade ticketWorkflowFacade,
                                         EdsDingtalkMessageFacade edsDingtalkMessageFacade, LanguageUtils languageUtils,
                                         NotificationTemplateService notificationTemplateService) {
        super(workOrderTicketEntryService, userService, ticketWorkflowFacade, edsDingtalkMessageFacade, languageUtils,
                notificationTemplateService);
    }

    public void sendMsg(WorkOrder workOrder, WorkOrderTicket ticket, String username, String password) {
        Map<String, Object> dict = SimpleMapBuilder.newBuilder()
                .put("username", username)
                .put("password", password)
                .build();
        sendMsgToUser(username, dict);
    }

    private void sendMsgToUser(String username, Map<String, Object> dict) {
        sendMsgToUsername(username, NotificationTemplateKeys.USER_RESET_PASSWORD_NOTICE.name(), dict);
    }

}
