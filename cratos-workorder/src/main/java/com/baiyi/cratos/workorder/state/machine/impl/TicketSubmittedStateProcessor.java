package com.baiyi.cratos.workorder.state.machine.impl;

import com.baiyi.cratos.common.builder.SimpleMapBuilder;
import com.baiyi.cratos.common.enums.NotificationTemplateKeys;
import com.baiyi.cratos.domain.generator.*;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.domain.util.BeanCopierUtil;
import com.baiyi.cratos.domain.util.LanguageUtils;
import com.baiyi.cratos.eds.core.facade.EdsDingtalkMessageFacade;
import com.baiyi.cratos.service.NotificationTemplateService;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.service.work.WorkOrderTicketNodeService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.workorder.annotation.TicketStates;
import com.baiyi.cratos.workorder.entry.TicketEntryProvider;
import com.baiyi.cratos.workorder.entry.TicketEntryProviderFactory;
import com.baiyi.cratos.workorder.enums.TicketState;
import com.baiyi.cratos.workorder.event.TicketEvent;
import com.baiyi.cratos.workorder.facade.TicketWorkflowFacade;
import com.baiyi.cratos.workorder.facade.WorkOrderTicketNodeFacade;
import com.baiyi.cratos.workorder.facade.WorkOrderTicketSubscriberFacade;
import com.baiyi.cratos.workorder.model.TicketEntryModel;
import com.baiyi.cratos.workorder.state.TicketStateChangeAction;
import com.baiyi.cratos.workorder.state.machine.BaseTicketStateProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/21 11:34
 * &#064;Version 1.0
 */
@Component
@TicketStates(state = TicketState.SUBMITTED, target = TicketState.IN_APPROVAL)
public class TicketSubmittedStateProcessor extends BaseTicketStateProcessor<WorkOrderTicketParam.SimpleTicketNo> {

    public TicketSubmittedStateProcessor(UserService userService, WorkOrderService workOrderService,
                                         WorkOrderTicketService workOrderTicketService,
                                         WorkOrderTicketNodeService workOrderTicketNodeService,
                                         WorkOrderTicketSubscriberFacade workOrderTicketSubscriberFacade,
                                         WorkOrderTicketNodeFacade workOrderTicketNodeFacade,
                                         WorkOrderTicketEntryService workOrderTicketEntryService,
                                         NotificationTemplateService notificationTemplateService,
                                         EdsDingtalkMessageFacade edsDingtalkMessageFacade, LanguageUtils languageUtils,
                                         TicketWorkflowFacade ticketWorkflowFacade) {
        super(userService, workOrderService, workOrderTicketService, workOrderTicketNodeService,
                workOrderTicketSubscriberFacade, workOrderTicketNodeFacade, workOrderTicketEntryService,
                notificationTemplateService, edsDingtalkMessageFacade, languageUtils, ticketWorkflowFacade);
    }

    @Override
    protected boolean isTransition(WorkOrderTicketParam.HasTicketNo hasTicketNo) {
        return true;
    }

    @Override
    protected boolean nextState(TicketStateChangeAction action,
                                TicketEvent<WorkOrderTicketParam.SimpleTicketNo> event) {
        return true;
    }

    @Override
    protected void processing(TicketStateChangeAction action, TicketEvent<WorkOrderTicketParam.SimpleTicketNo> event) {
        // 发布订阅关系
        WorkOrderTicket ticket = getTicketByNo(event.getBody()
                .getTicketNo());
        workOrderTicketSubscriberFacade.publish(ticket);
        // 发送通知
        if (ticket.getNodeId() == 0) {
            // 无审批节点
            return;
        }
        WorkOrderTicketNode ticketNode = workOrderTicketNodeService.getById(ticket.getNodeId());
        WorkOrder workOrder = workOrderService.getById(ticket.getWorkOrderId());
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
                .put("ticketEntities",ticketEntities)
                .build();
        sendMsgToApprover(approvers, dict);
    }

    private List<User> queryApprovers(WorkOrder workOrder, WorkOrderTicket ticket, WorkOrderTicketNode ticketNode) {
        if (StringUtils.hasText(ticketNode.getUsername())) {
            return List.of(userService.getByUsername(ticketNode.getUsername()));
        }
        return BeanCopierUtil.copyListProperties(
                ticketWorkflowFacade.queryNodeApprovalUsers(workOrder, ticketNode.getNodeName()), User.class);
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

