package com.baiyi.cratos.workorder.state.machine.impl;

import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.workorder.annotation.StateForward;
import com.baiyi.cratos.workorder.annotation.TicketStates;
import com.baiyi.cratos.workorder.annotation.TransitionGuard;
import com.baiyi.cratos.workorder.context.TicketStateProcessorContext;
import com.baiyi.cratos.workorder.enums.TicketState;
import com.baiyi.cratos.workorder.enums.TicketStateChangeAction;
import com.baiyi.cratos.workorder.event.TicketEvent;
import com.baiyi.cratos.workorder.state.machine.BaseTicketStateProcessor;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/21 13:48
 * &#064;Version 1.0
 */
@Component
@TicketStates(state = TicketState.COMPLETED)
@TransitionGuard(enabled = false)
@StateForward(enabled = false)
public class TicketCompletedStateProcessor extends BaseTicketStateProcessor<WorkOrderTicketParam.SimpleTicketNo> {

    public TicketCompletedStateProcessor(TicketStateProcessorContext context) {
        super(context);
    }

    @Override
    protected void processing(TicketStateChangeAction action, TicketEvent<WorkOrderTicketParam.SimpleTicketNo> event) {
        WorkOrderTicket ticket = getTicketByNo(event.getBody());
        ticket.setCompletedAt(new Date());
        ticket.setCompleted(true);
        context.getWorkOrderTicketService()
                .updateByPrimaryKey(ticket);
    }

}
