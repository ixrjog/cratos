package com.baiyi.cratos.workorder.state.machine;

import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.workorder.state.TicketState;
import com.baiyi.cratos.workorder.state.TicketStateChangeAction;
import com.baiyi.cratos.workorder.state.machine.factory.TicketInStateProcessorFactory;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/21 11:30
 * &#064;Version 1.0
 */
@RequiredArgsConstructor
public abstract class BaseTicketStateProcessor implements TicketStateProcessor {

    private TicketStateProcessor targetProcessor;
    private final WorkOrderTicketService workOrderTicketService;

    @Override
    public TicketStateProcessor setTarget(TicketStateProcessor processor) {
        this.targetProcessor = processor;
        return this.targetProcessor;
    }

    @Override
    public TicketStateProcessor getByState(TicketState ticketState) {
        return getState().equals(ticketState) ? this : this.targetProcessor.getByState(ticketState);
    }

    @Override
    public TicketStateProcessor getTarget() {
        return targetProcessor;
    }

    @Override
    public void change(WorkOrderTicket ticket, TicketStateChangeAction action) {

           setNextState(ticket);
    }

    protected void setNextState(WorkOrderTicket ticket) {
        TicketStateProcessor processor = TicketInStateProcessorFactory.getByState(
                TicketState.valueOf(ticket.getTicketState()));
        TicketStateProcessor nextProcessor = getTarget();
        if (Objects.nonNull(nextProcessor)) {
            ticket.setTicketState(nextProcessor.getState()
                    .name());
            workOrderTicketService.updateByPrimaryKey(ticket);
        }
    }

}
