package com.baiyi.cratos.workorder.util;

import com.baiyi.cratos.common.builder.SimpleMapBuilder;
import com.baiyi.cratos.common.enums.NotificationTemplateKeys;
import com.baiyi.cratos.common.util.beetl.BeetlUtil;
import com.baiyi.cratos.domain.generator.*;
import com.baiyi.cratos.domain.util.BeanCopierUtil;
import com.baiyi.cratos.domain.util.LanguageUtils;
import com.baiyi.cratos.eds.core.facade.EdsDingtalkMessageFacade;
import com.baiyi.cratos.service.NotificationTemplateService;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.workorder.entry.TicketEntryProvider;
import com.baiyi.cratos.workorder.entry.TicketEntryProviderFactory;
import com.baiyi.cratos.workorder.facade.TicketWorkflowFacade;
import com.baiyi.cratos.workorder.model.TicketEntryModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/31 14:17
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ApprovalNotificationHelper {

    private final WorkOrderTicketEntryService workOrderTicketEntryService;
    private final UserService userService;
    private final TicketWorkflowFacade ticketWorkflowFacade;
    private final EdsDingtalkMessageFacade edsDingtalkMessageFacade;
    private final LanguageUtils languageUtils;
    private final NotificationTemplateService notificationTemplateService;

    public void sendMsg(WorkOrder workOrder, WorkOrderTicket ticket, WorkOrderTicketNode ticketNode) {
        List<User> approvers = queryApprovers(workOrder, ticket, ticketNode);
        List<TicketEntryModel.EntryDesc> ticketEntities = workOrderTicketEntryService.queryTicketEntries(ticket.getId())
                .stream()
                .map(entry -> {
                    TicketEntryProvider<?, ?> ticketEntryProvider = TicketEntryProviderFactory.getByBusinessType(
                            entry.getBusinessType());
                    return ticketEntryProvider.getEntryDesc(entry);
                })
                .toList();
        Map<String, Object> dict = SimpleMapBuilder.newBuilder()
                .put("ticketNo", ticket.getTicketNo())
                .put("workOrderName", workOrder.getName())
                .put("applicant", ticket.getUsername())
                .put("ticketEntities", ticketEntities)
                .build();
        sendMsgToApprover(approvers, dict);
    }

    private List<User> queryApprovers(WorkOrder workOrder, WorkOrderTicket ticket, WorkOrderTicketNode ticketNode) {
        if (StringUtils.hasText(ticketNode.getUsername())) {
            return List.of(userService.getByUsername(ticketNode.getUsername()));
        }
        return BeanCopierUtil.copyListProperties(
                ticketWorkflowFacade.queryNodeApprovalUsers(ticket, ticketNode.getNodeName()), User.class);
    }

    private void sendMsgToApprover(List<User> approvers, Map<String, Object> dict) {
        if (CollectionUtils.isEmpty(approvers)) {
            return;
        }
        approvers.forEach(
                approver -> sendMsgToUser(approver, NotificationTemplateKeys.WORK_ORDER_TICKET_APPROVAL_NOTICE.name(),
                        dict));
    }

    protected void sendMsgToUser(User sendToUser, String notificationTemplateKey, Map<String, Object> dict) {
        try {
            NotificationTemplate notificationTemplate = getNotificationTemplate(notificationTemplateKey, sendToUser);
            String msg = BeetlUtil.renderTemplate(notificationTemplate.getContent(), dict);
            edsDingtalkMessageFacade.sendToDingtalkUser(sendToUser, notificationTemplate, msg);
        } catch (IOException ioException) {
            log.error("WorkOrder ticket send msg to user err: {}", ioException.getMessage());
        }
    }

    private NotificationTemplate getNotificationTemplate(String notificationTemplateKey, User user) {
        NotificationTemplate query = NotificationTemplate.builder()
                .notificationTemplateKey(notificationTemplateKey)
                .lang(languageUtils.getUserLanguage(user))
                .build();
        return notificationTemplateService.getByUniqueKey(query);
    }

}
