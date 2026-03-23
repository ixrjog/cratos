package com.baiyi.cratos.workorder.state.machine.impl;

import com.baiyi.cratos.domain.generator.WorkOrder;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.domain.generator.WorkOrderTicketNode;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.workorder.annotation.StateForward;
import com.baiyi.cratos.workorder.annotation.TicketStates;
import com.baiyi.cratos.workorder.annotation.TransitionGuard;
import com.baiyi.cratos.workorder.context.TicketStateProcessorContext;
import com.baiyi.cratos.workorder.enums.TicketState;
import com.baiyi.cratos.workorder.enums.TicketStateChangeAction;
import com.baiyi.cratos.workorder.event.TicketEvent;
import com.baiyi.cratos.workorder.notice.WorkOrderApprovalNoticeSender;
import com.baiyi.cratos.workorder.state.machine.BaseTicketStateProcessor;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/21 11:34
 * &#064;Version 1.0
 */
@Component
@TicketStates(state = TicketState.SUBMITTED, target = TicketState.IN_APPROVAL)
@TransitionGuard
@StateForward
public class TicketSubmittedStateProcessor extends BaseTicketStateProcessor<WorkOrderTicketParam.SimpleTicketNo> {

    private final WorkOrderApprovalNoticeSender workOrderApprovalNoticeSender;

    public TicketSubmittedStateProcessor(TicketStateProcessorContext context,
                                         WorkOrderApprovalNoticeSender workOrderApprovalNoticeSender) {
        super(context);
        this.workOrderApprovalNoticeSender = workOrderApprovalNoticeSender;
    }

    @Override
    protected void processing(TicketStateChangeAction action, TicketEvent<WorkOrderTicketParam.SimpleTicketNo> event) {
        // 发布订阅关系
        WorkOrderTicket ticket = getTicketByNo(event.getBody()
                                                       .getTicketNo());
        context.getWorkOrderTicketSubscriberFacade()
                .publish(ticket);
        // 发送通知
        if (ticket.getNodeId() == 0) {
            // 无审批节点
            return;
        }
        WorkOrderTicketNode ticketNode = context.getWorkOrderTicketNodeService()
                .getById(ticket.getNodeId());
        WorkOrder workOrder = context.getWorkOrderService()
                .getById(ticket.getWorkOrderId());
        workOrderApprovalNoticeSender.sendMsg(workOrder, ticket, ticketNode);
    }

}

