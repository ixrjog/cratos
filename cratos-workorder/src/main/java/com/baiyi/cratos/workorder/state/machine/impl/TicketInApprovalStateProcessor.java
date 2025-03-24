package com.baiyi.cratos.workorder.state.machine.impl;

import com.baiyi.cratos.common.exception.WorkOrderTicketException;
import com.baiyi.cratos.common.util.SessionUtils;
import com.baiyi.cratos.domain.generator.WorkOrder;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.domain.generator.WorkOrderTicketNode;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.service.work.WorkOrderTicketNodeService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.workorder.annotation.TicketStates;
import com.baiyi.cratos.workorder.builder.TicketNodeUpdater;
import com.baiyi.cratos.workorder.enums.ApprovalStatus;
import com.baiyi.cratos.workorder.enums.ApprovalTypes;
import com.baiyi.cratos.workorder.event.TicketEvent;
import com.baiyi.cratos.workorder.facade.TicketWorkflowFacade;
import com.baiyi.cratos.workorder.facade.WorkOrderTicketNodeFacade;
import com.baiyi.cratos.workorder.facade.WorkOrderTicketSubscriberFacade;
import com.baiyi.cratos.workorder.state.TicketState;
import com.baiyi.cratos.workorder.state.TicketStateChangeAction;
import com.baiyi.cratos.workorder.state.machine.BaseTicketStateProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/21 11:35
 * &#064;Version 1.0
 */
@Component
@TicketStates(state = TicketState.IN_APPROVAL)
public class TicketInApprovalStateProcessor extends BaseTicketStateProcessor<WorkOrderTicketParam.ApprovalTicket> {

    private final TicketWorkflowFacade ticketWorkflowFacade;

    public TicketInApprovalStateProcessor(UserService userService, WorkOrderService workOrderService,
                                          WorkOrderTicketService workOrderTicketService,
                                          WorkOrderTicketNodeService workOrderTicketNodeService,
                                          WorkOrderTicketSubscriberFacade workOrderTicketSubscriberFacade,
                                          WorkOrderTicketNodeFacade workOrderTicketNodeFacade,
                                          WorkOrderTicketEntryService workOrderTicketEntryService,
                                          TicketWorkflowFacade ticketWorkflowFacade) {
        super(userService, workOrderService, workOrderTicketService, workOrderTicketNodeService,
                workOrderTicketSubscriberFacade, workOrderTicketNodeFacade, workOrderTicketEntryService);
        this.ticketWorkflowFacade = ticketWorkflowFacade;
    }

    @Override
    protected void preChangeInspection(WorkOrderTicket ticket, TicketStateChangeAction action,
                                       TicketEvent<WorkOrderTicketParam.ApprovalTicket> event) {
        super.preChangeInspection(ticket, action, event);
        // 是否当前审批人
        WorkOrder workOrder = workOrderService.getById(ticket.getWorkOrderId());
        WorkOrderTicketNode ticketNode = workOrderTicketNodeService.getById(ticket.getNodeId());
        if (ticketNode.getApprovalCompleted()) {
            WorkOrderTicketException.runtime("The current node has been approved.");
        }
        if (ApprovalTypes.USER_SPECIFIED.name()
                .equals(ticketNode.getApprovalType())) {
            if (!ticketNode.getUsername()
                    .equals(SessionUtils.getUsername())) {
                WorkOrderTicketException.runtime("You are not the current approver.");
            }
            return;
        }
        if (!ticketWorkflowFacade.isApprover(workOrder, ticketNode.getNodeName(), SessionUtils.getUsername())) {
            WorkOrderTicketException.runtime("You are not the current approver.");
        }
    }

    @Override
    protected boolean isTransition(WorkOrderTicket ticket) {
        List<WorkOrderTicketNode> ticketNodes = workOrderTicketNodeService.queryByTicketId(ticket.getId());
        return CollectionUtils.isEmpty(ticketNodes) || ticketNodes.stream()
                .allMatch(ticketNode -> Boolean.TRUE.equals(ticketNode.getApprovalCompleted()));
    }

    @Override
    protected void processing(WorkOrderTicket ticket, TicketStateChangeAction action,
                              TicketEvent<WorkOrderTicketParam.ApprovalTicket> event) {
        ApprovalStatus approvalStatus = ApprovalStatus.valueOf(event.getBody()
                .getApprovalType());
        if (ApprovalStatus.AGREE.equals(approvalStatus)) {
            WorkOrderTicketNode ticketNode = workOrderTicketNodeService.getById(ticket.getNodeId());
            // 更新审批节点
            TicketNodeUpdater.newUpdater()
                    .withApprovalTicket(event.getBody())
                    .withUsername(SessionUtils.getUsername())
                    .withNode(ticketNode)
                    .withService(workOrderTicketNodeService)
                    .updateNode();
        }

        if (ApprovalStatus.REJECT.equals(approvalStatus)) {

        }
    }

}
