package com.baiyi.cratos.workorder.state.machine.impl;

import com.baiyi.cratos.common.util.SessionUtils;
import com.baiyi.cratos.domain.generator.WorkOrder;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.domain.generator.WorkOrderTicketNode;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.workorder.annotation.TicketStates;
import com.baiyi.cratos.workorder.builder.TicketNodeUpdater;
import com.baiyi.cratos.workorder.context.TicketStateProcessorContext;
import com.baiyi.cratos.workorder.enums.ApprovalStatus;
import com.baiyi.cratos.workorder.enums.ApprovalTypes;
import com.baiyi.cratos.workorder.enums.TicketState;
import com.baiyi.cratos.workorder.enums.TicketStateChangeAction;
import com.baiyi.cratos.workorder.event.TicketEvent;
import com.baiyi.cratos.workorder.exception.WorkOrderTicketDoNextException;
import com.baiyi.cratos.workorder.exception.WorkOrderTicketException;
import com.baiyi.cratos.workorder.notice.WorkOrderApprovalNoticeSender;
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
public class TicketInApprovalStateProcessor extends BaseTicketStateProcessor<WorkOrderTicketParam.SimpleTicketNo> {

    private final WorkOrderApprovalNoticeSender workOrderApprovalNoticeSender;

    public TicketInApprovalStateProcessor(TicketStateProcessorContext context,
                                          WorkOrderApprovalNoticeSender workOrderApprovalNoticeSender) {
        super(context);
        this.workOrderApprovalNoticeSender = workOrderApprovalNoticeSender;
    }

    @Override
    protected void preChangeInspection(TicketStateChangeAction action,
                                       TicketEvent<WorkOrderTicketParam.SimpleTicketNo> event) {
        super.preChangeInspection(action, event);
        WorkOrderTicket ticket = context.getWorkOrderTicketService()
                .getByTicketNo(event.getBody()
                                       .getTicketNo());
        // WorkOrder workOrder = workOrderService.getById(ticket.getWorkOrderId());
        // 工单无审批节点
        if (ticket.getNodeId() == 0) {
            return;
        } else if (TicketStateChangeAction.DO_NEXT.equals(action)) {
            WorkOrderTicketDoNextException.runtime("There are approval nodes.");
        }
        if (event.getBody() instanceof WorkOrderTicketParam.ApprovalTicket) {
            // 是否当前审批人
            WorkOrderTicketNode ticketNode = context.getWorkOrderTicketNodeService()
                    .getById(ticket.getNodeId());
            if (Boolean.TRUE.equals(ticketNode.getApprovalCompleted())) {
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
            if (!context.getTicketWorkflowFacade()
                    .isApprover(ticket, ticketNode.getNodeName(), SessionUtils.getUsername())) {
                WorkOrderTicketException.runtime("You are not the current approver.");
            }
        }
    }

    @Override
    protected boolean nextState(TicketStateChangeAction action,
                                TicketEvent<WorkOrderTicketParam.SimpleTicketNo> event) {
        // 判断是否审批完成
        WorkOrderTicket ticket = context.getWorkOrderTicketService()
                .getByTicketNo(event.getBody()
                                       .getTicketNo());
        // 工单无审批节点
        if (ticket.getNodeId() == 0) {
            return true;
        }
        // Approval Completed
        return context.getWorkOrderTicketNodeService()
                .queryByTicketId(ticket.getId())
                .stream()
                .allMatch(e -> Boolean.TRUE.equals(e.getApprovalCompleted()));
    }

    @Override
    protected boolean isTransition(WorkOrderTicketParam.HasTicketNo hasTicketNo) {
        WorkOrderTicket ticket = getTicketByNo(hasTicketNo);
        List<WorkOrderTicketNode> ticketNodes = context.getWorkOrderTicketNodeService()
                .queryByTicketId(ticket.getId());
        return CollectionUtils.isEmpty(ticketNodes) || ticketNodes.stream()
                .allMatch(ticketNode -> Boolean.TRUE.equals(ticketNode.getApprovalCompleted()));
    }

    @Override
    protected void processing(TicketStateChangeAction action, TicketEvent<WorkOrderTicketParam.SimpleTicketNo> event) {
        if (event.getBody() instanceof WorkOrderTicketParam.ApprovalTicket approvalTicket) {
            ApprovalStatus approvalStatus = ApprovalStatus.valueOf(approvalTicket.getApprovalType());
            WorkOrderTicket ticket = getTicketByNo(event.getBody());
            WorkOrderTicketNode ticketNode = context.getWorkOrderTicketNodeService()
                    .getById(ticket.getNodeId());
            // 更新审批节点
            TicketNodeUpdater.newUpdater()
                    .withApprovalTicket(approvalTicket)
                    .withUsername(SessionUtils.getUsername())
                    .withNode(ticketNode)
                    .withService(context.getWorkOrderTicketNodeService())
                    .updateNode();
            // 更新工单节点ID
            if (ApprovalStatus.AGREE.equals(approvalStatus)) {
                approveAgree(ticket, ticketNode);
                return;
            }
            if (ApprovalStatus.REJECT.equals(approvalStatus)) {
                approveReject(ticket, approvalTicket);
                return;
            }
            WorkOrderTicketException.runtime("Incorrect approval type.");
        }
    }

    protected void sendMsg(WorkOrderTicket ticket, WorkOrderTicketNode nextNode) {
        WorkOrder workOrder = context.getWorkOrderService()
                .getById(ticket.getWorkOrderId());
        workOrderApprovalNoticeSender.sendMsg(workOrder, ticket, nextNode);
    }

    private void approveAgree(WorkOrderTicket ticket, WorkOrderTicketNode ticketNode) {
        WorkOrderTicketNode nextNode = context.getWorkOrderTicketNodeService()
                .getByTicketParentId(ticket.getId(), ticketNode.getId());
        if (Objects.nonNull(nextNode)) {
            ticket.setNodeId(nextNode.getId());
            context.getWorkOrderTicketService()
                    .updateByPrimaryKey(ticket);
            sendMsg(ticket, nextNode);
        }
    }

    private void approveReject(WorkOrderTicket ticket, WorkOrderTicketParam.ApprovalTicket approvalTicket) {
        ticket.setCompleted(true);
        ticket.setCompletedAt(new Date());
        ticket.setTicketState(TicketState.COMPLETED.name());
        ticket.setSuccess(false);
        ticket.setTicketResult(StringUtils.hasText(
                approvalTicket.getApproveRemark()) ? approvalTicket.getApproveRemark() : "Approval rejected.");
        context.getWorkOrderTicketService()
                .updateByPrimaryKey(ticket);
    }

}
