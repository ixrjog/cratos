package com.baiyi.cratos.workorder.notice;

import com.baiyi.cratos.common.builder.SimpleMapBuilder;
import com.baiyi.cratos.common.enums.NotificationTemplateKeys;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.generator.WorkOrder;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.util.LanguageUtils;
import com.baiyi.cratos.eds.core.facade.EdsDingtalkMessageFacade;
import com.baiyi.cratos.service.NotificationTemplateService;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.workorder.facade.TicketWorkflowFacade;
import com.baiyi.cratos.workorder.notice.base.BaseWorkOrderNoticeSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/5/29 14:07
 * &#064;Version 1.0
 */
@Slf4j
@Component
public class ResetAwsIamUserNoticeSender extends BaseWorkOrderNoticeSender {

    public ResetAwsIamUserNoticeSender(WorkOrderTicketEntryService workOrderTicketEntryService,
                                          UserService userService, TicketWorkflowFacade ticketWorkflowFacade,
                                          EdsDingtalkMessageFacade edsDingtalkMessageFacade,
                                          LanguageUtils languageUtils,
                                          NotificationTemplateService notificationTemplateService) {
        super(workOrderTicketEntryService, userService, ticketWorkflowFacade, edsDingtalkMessageFacade, languageUtils,
                notificationTemplateService);
    }

    public void sendMsg(WorkOrder workOrder, WorkOrderTicket ticket,String accountId, String iamLoginUsername, String password,
                        String loginLink, User applicantUser) {
        Map<String, Object> dict = SimpleMapBuilder.newBuilder()
                .put("ticketNo", ticket.getTicketNo())
                .put("workOrderName", workOrder.getName())
                .put("applicant", ticket.getUsername())
                .put("accountId",accountId)
                .put("aliyunRAMUsername", iamLoginUsername)
                .put("password", password)
                .put("loginLink", loginLink)
                .build();
        sendMsgToUser(applicantUser, dict);
    }

    private void sendMsgToUser(User sendToUser, Map<String, Object> dict) {
        sendMsgToUser(sendToUser, NotificationTemplateKeys.RESET_AWS_IAM_USER_NOTICE, dict);
    }

}
