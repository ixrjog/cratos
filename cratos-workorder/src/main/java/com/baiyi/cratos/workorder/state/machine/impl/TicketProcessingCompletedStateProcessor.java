package com.baiyi.cratos.workorder.state.machine.impl;

import com.baiyi.cratos.domain.generator.WorkOrder;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.workorder.annotation.StateForward;
import com.baiyi.cratos.workorder.annotation.TicketStates;
import com.baiyi.cratos.workorder.annotation.TransitionGuard;
import com.baiyi.cratos.workorder.context.TicketStateProcessorContext;
import com.baiyi.cratos.workorder.enums.TicketState;
import com.baiyi.cratos.workorder.enums.TicketStateChangeAction;
import com.baiyi.cratos.workorder.event.TicketEvent;
import com.baiyi.cratos.workorder.notice.WorkOrderCompletionNoticeSender;
import com.baiyi.cratos.workorder.state.machine.BaseTicketStateProcessor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/21 13:47
 * &#064;Version 1.0
 */
@Component
@TicketStates(state = TicketState.PROCESSING_COMPLETED, target = TicketState.COMPLETED)
@TransitionGuard
@StateForward
public class TicketProcessingCompletedStateProcessor extends BaseTicketStateProcessor<WorkOrderTicketParam.SimpleTicketNo> {

    private final WorkOrderCompletionNoticeSender workOrderCompletionNoticeSender;

    public TicketProcessingCompletedStateProcessor(TicketStateProcessorContext context,
                                                   WorkOrderCompletionNoticeSender workOrderCompletionNoticeSender) {
        super(context);
        this.workOrderCompletionNoticeSender = workOrderCompletionNoticeSender;
    }


    @Override
    protected void processing(TicketStateChangeAction action, TicketEvent<WorkOrderTicketParam.SimpleTicketNo> event) {
        // 设置工单success
        WorkOrderTicket ticket = getTicketByNo(event.getBody());
        List<WorkOrderTicketEntry> entries = context.getWorkOrderTicketEntryService()
                .queryTicketEntries(ticket.getId());
        boolean success = entries.stream()
                .allMatch(e -> Boolean.TRUE.equals(e.getSuccess()));
        ticket.setSuccess(success);
        context.getWorkOrderTicketService()
                .updateByPrimaryKey(ticket);
        // 工单处理完成通知
        WorkOrder workOrder = context.getWorkOrderService()
                .getById(ticket.getWorkOrderId());
        workOrderCompletionNoticeSender.sendMsg(workOrder, ticket);
    }

}
