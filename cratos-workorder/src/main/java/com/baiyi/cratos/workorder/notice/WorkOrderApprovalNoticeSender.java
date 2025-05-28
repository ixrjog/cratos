package com.baiyi.cratos.workorder.notice;

import com.baiyi.cratos.common.builder.SimpleMapBuilder;
import com.baiyi.cratos.common.enums.NotificationTemplateKeys;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.generator.WorkOrder;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.domain.generator.WorkOrderTicketNode;
import com.baiyi.cratos.domain.util.LanguageUtils;
import com.baiyi.cratos.eds.core.facade.EdsDingtalkMessageFacade;
import com.baiyi.cratos.service.NotificationTemplateService;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.workorder.entry.TicketEntryProvider;
import com.baiyi.cratos.workorder.entry.TicketEntryProviderFactory;
import com.baiyi.cratos.workorder.facade.TicketWorkflowFacade;
import com.baiyi.cratos.workorder.model.TicketEntryModel;
import com.baiyi.cratos.workorder.notice.base.BaseWorkOrderNoticeHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/31 14:17
 * &#064;Version 1.0
 */
@Slf4j
@Component
public class WorkOrderApprovalNoticeSender extends BaseWorkOrderNoticeHelper {

    public WorkOrderApprovalNoticeSender(WorkOrderTicketEntryService workOrderTicketEntryService,
                                         UserService userService, TicketWorkflowFacade ticketWorkflowFacade,
                                         EdsDingtalkMessageFacade edsDingtalkMessageFacade, LanguageUtils languageUtils,
                                         NotificationTemplateService notificationTemplateService) {
        super(workOrderTicketEntryService, userService, ticketWorkflowFacade, edsDingtalkMessageFacade, languageUtils,
                notificationTemplateService);
    }

    public void sendMsg(WorkOrder workOrder, WorkOrderTicket ticket, WorkOrderTicketNode ticketNode) {
        List<User> approvers = queryApprovers(workOrder, ticket, ticketNode);
        List<TicketEntryModel.EntryDesc> ticketEntities = workOrderTicketEntryService.queryTicketEntries(ticket.getId())
                .stream()
                .map(entry -> {
                    TicketEntryProvider<?, ?> ticketEntryProvider = TicketEntryProviderFactory.getProvider(
                            workOrder.getWorkOrderKey(), entry.getBusinessType());
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
        return ticketWorkflowFacade.queryNodeApprovalUsers(ticket, ticketNode.getNodeName());
    }

    private void sendMsgToApprover(List<User> approvers, Map<String, Object> dict) {
        if (CollectionUtils.isEmpty(approvers)) {
            return;
        }
        approvers.forEach(
                approver -> sendMsgToUser(approver, NotificationTemplateKeys.WORK_ORDER_TICKET_APPROVAL_NOTICE.name(),
                        dict));
    }

}
