package com.baiyi.cratos.workorder.notice;

import com.baiyi.cratos.common.builder.SimpleMapBuilder;
import com.baiyi.cratos.common.enums.NotificationTemplateKeys;
import com.baiyi.cratos.domain.generator.*;
import com.baiyi.cratos.domain.model.WorkflowModel;
import com.baiyi.cratos.eds.core.facade.EdsDingtalkMessageFacade;
import com.baiyi.cratos.service.NotificationTemplateService;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.service.work.WorkOrderTicketSubscriberService;
import com.baiyi.cratos.util.LanguageUtils;
import com.baiyi.cratos.workorder.entry.TicketEntryProvider;
import com.baiyi.cratos.workorder.entry.TicketEntryProviderFactory;
import com.baiyi.cratos.workorder.enums.SubscribeStatus;
import com.baiyi.cratos.workorder.facade.TicketWorkflowFacade;
import com.baiyi.cratos.workorder.model.TicketEntryModel;
import com.baiyi.cratos.workorder.notice.base.BaseWorkOrderNoticeSender;
import com.baiyi.cratos.workorder.util.WorkflowUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/31 14:17
 * &#064;Version 1.0
 */
@Slf4j
@Component
public class WorkOrderApprovalNoticeSender extends BaseWorkOrderNoticeSender {

    private final WorkOrderTicketSubscriberService workOrderTicketSubscriberService;

    public WorkOrderApprovalNoticeSender(WorkOrderTicketEntryService workOrderTicketEntryService,
                                         UserService userService, TicketWorkflowFacade ticketWorkflowFacade,
                                         EdsDingtalkMessageFacade edsDingtalkMessageFacade, LanguageUtils languageUtils,
                                         NotificationTemplateService notificationTemplateService,
                                         WorkOrderTicketSubscriberService workOrderTicketSubscriberService) {
        super(workOrderTicketEntryService, userService, ticketWorkflowFacade, edsDingtalkMessageFacade, languageUtils,
                notificationTemplateService);
        this.workOrderTicketSubscriberService = workOrderTicketSubscriberService;
    }

    public void sendMsg(WorkOrder workOrder, WorkOrderTicket ticket, WorkOrderTicketNode ticketNode) {
        List<User> approvers = queryApprovers(workOrder, ticket, ticketNode);
        List<TicketEntryModel.EntryDesc> ticketEntities = workOrderTicketEntryService.queryTicketEntries(ticket.getId())
                .stream()
                .map(entry -> {
                    TicketEntryProvider<?, ?> provider = TicketEntryProviderFactory.getProvider(
                            workOrder.getWorkOrderKey(), entry.getBusinessType());
                    return provider != null ? provider.getEntryDesc(entry) : null;
                })
                .filter(java.util.Objects::nonNull)
                .toList();
        // /workorder/ticket/approval?ticketNo=${ticketNo}&username=${approver}&approvalType=AGREE&token=${token}
        // /workorder/ticket/approval?ticketNo=${ticketNo}&username=${approver}&approvalType=REJECT&token=${token}
        Map<String, Object> dict = SimpleMapBuilder.newBuilder()
                .put("ticketNo", ticket.getTicketNo())
                .put("workOrderName", workOrder.getName())
                .put("applicant", ticket.getUsername())
                .put("ticketEntities", ticketEntities)
                .build();
        sendMsgToApprover(ticket, ticketNode, approvers, dict);
    }

    private List<User> queryApprovers(WorkOrder workOrder, WorkOrderTicket ticket, WorkOrderTicketNode ticketNode) {
        if (StringUtils.hasText(ticketNode.getUsername())) {
            return List.of(userService.getByUsername(ticketNode.getUsername()));
        }
        return ticketWorkflowFacade.queryNodeApprovalUsers(ticket, ticketNode.getNodeName());
    }

    private void sendMsgToApprover(WorkOrderTicket ticket, WorkOrderTicketNode ticketNode, List<User> approvers,
                                   Map<String, Object> dict) {
        if (CollectionUtils.isEmpty(approvers)) {
            return;
        }
        approvers.forEach(approver -> {
            // 审批节点名称
            String approvalNode = "--";
            WorkflowModel.Workflow workflow = WorkflowUtils.loadAs(ticket);
            Optional<WorkflowModel.Node> optionalNode = WorkflowUtils.getNodeByName(workflow, ticketNode);
            if (optionalNode.isPresent()) {
                WorkflowModel.Node node = optionalNode.get();
                String lang = languageUtils.getLanguageOf(approver);
                approvalNode = node.getLangMap()
                        .getOrDefault(lang, null) != null ? node.getLangMap()
                        .get(lang)
                        .getDisplayName() : "--";
            }
            // 手机审批按钮
            WorkOrderTicketSubscriber uk = WorkOrderTicketSubscriber.builder()
                    .ticketId(ticket.getId())
                    .username(approver.getUsername())
                    .subscribeStatus(SubscribeStatus.APPROVED_BY.name())
                    .build();
            WorkOrderTicketSubscriber workOrderTicketSubscriber = workOrderTicketSubscriberService.getByUniqueKey(uk);
            Map<String, Object> approverDict = new java.util.HashMap<>(dict);
            approverDict.put("approver", approver.getUsername());
            approverDict.put("token", workOrderTicketSubscriber.getToken());
            approverDict.put("approvalNode", approvalNode);
            sendMsgToUser(approver, NotificationTemplateKeys.WORK_ORDER_TICKET_APPROVAL_NOTICE.name(), approverDict);
        });
    }

}
