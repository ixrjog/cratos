package com.baiyi.cratos.workorder.state.machine.impl;

import com.baiyi.cratos.workorder.exception.WorkOrderTicketException;
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
import com.baiyi.cratos.workorder.enums.TicketState;
import com.baiyi.cratos.workorder.state.TicketStateChangeAction;
import com.baiyi.cratos.workorder.state.machine.BaseTicketStateProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/21 11:35
 * &#064;Version 1.0
 */
@Component
@TicketStates(state = TicketState.IN_APPROVAL, target = TicketState.APPROVAL_COMPLETED)
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
    protected void preChangeInspection(TicketStateChangeAction action,
                                       TicketEvent<WorkOrderTicketParam.ApprovalTicket> event) {
        super.preChangeInspection(action, event);
        // 是否当前审批人
        WorkOrderTicket ticket = workOrderTicketService.getByTicketNo(event.getBody()
                .getTicketNo());
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

    protected boolean nextState() {
        return false;
    }

    @Override
    protected boolean isTransition(WorkOrderTicketParam.HasTicketNo hasTicketNo) {
        WorkOrderTicket ticket = getTicketByNo(hasTicketNo);
        List<WorkOrderTicketNode> ticketNodes = workOrderTicketNodeService.queryByTicketId(ticket.getId());
        return CollectionUtils.isEmpty(ticketNodes) || ticketNodes.stream()
                .allMatch(ticketNode -> Boolean.TRUE.equals(ticketNode.getApprovalCompleted()));
    }

    @Override
    protected void processing(TicketStateChangeAction action, TicketEvent<WorkOrderTicketParam.ApprovalTicket> event) {
        ApprovalStatus approvalStatus = ApprovalStatus.valueOf(event.getBody()
                .getApprovalType());
        WorkOrderTicket ticket = getTicketByNo(event.getBody());
        WorkOrderTicketNode ticketNode = workOrderTicketNodeService.getById(ticket.getNodeId());
        // 更新审批节点
        TicketNodeUpdater.newUpdater()
                .withApprovalTicket(event.getBody())
                .withUsername(SessionUtils.getUsername())
                .withNode(ticketNode)
                .withService(workOrderTicketNodeService)
                .updateNode();
        // 更新工单节点ID
        if (ApprovalStatus.AGREE.equals(approvalStatus)) {
            approveAgree(ticket, ticketNode);
            return;
        }
        if (ApprovalStatus.REJECT.equals(approvalStatus)) {
            approveReject(ticket, event);
        }
    }

    private void approveAgree(WorkOrderTicket ticket, WorkOrderTicketNode ticketNode) {
        WorkOrderTicketNode nextNode = workOrderTicketNodeService.getByTicketParentId(ticket.getId(),
                ticketNode.getId());
        if (Objects.nonNull(nextNode)) {
            ticket.setNodeId(nextNode.getId());
            workOrderTicketService.updateByPrimaryKey(ticket);
        }
    }

    private void approveReject(WorkOrderTicket ticket, TicketEvent<WorkOrderTicketParam.ApprovalTicket> event) {
        ticket.setCompleted(true);
        ticket.setCompletedAt(new Date());
        ticket.setTicketState(TicketState.COMPLETED.name());
        ticket.setSuccess(false);
        ticket.setTicketResult(StringUtils.hasText(event.getBody()
                .getApproveRemark()) ? event.getBody()
                .getApproveRemark() : "Approval rejected.");
        workOrderTicketService.updateByPrimaryKey(ticket);
    }

}
