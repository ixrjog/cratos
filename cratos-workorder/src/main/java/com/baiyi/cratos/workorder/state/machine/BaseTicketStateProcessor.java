package com.baiyi.cratos.workorder.state.machine;

import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.workorder.event.TicketEvent;
import com.baiyi.cratos.workorder.exception.TicketStateProcessorException;
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
@SuppressWarnings("unchecked")
@RequiredArgsConstructor
public abstract class BaseTicketStateProcessor<Body> implements TicketStateProcessor<Body> {

    private TicketStateProcessor<Body> targetProcessor;
    protected final WorkOrderTicketService workOrderTicketService;

    @SuppressWarnings("rawtypes")
    @Override
    public TicketStateProcessor setTarget(TicketStateProcessor processor) {
        this.targetProcessor = processor;
        return this.targetProcessor;
    }

    @Override
    public TicketStateProcessor<Body> getByState(TicketState ticketState) {
        return getState().equals(ticketState) ? this : this.targetProcessor.getByState(ticketState);
    }

    @Override
    public TicketStateProcessor<Body> getTarget() {
        return targetProcessor;
    }

    protected void preChangeInspection(WorkOrderTicket ticket, TicketStateChangeAction action,
                                       TicketEvent<Body> event) {
        if (getState() != TicketState.valueOf(ticket.getTicketState())) {
            TicketStateProcessorException.runtime(
                    "The work order status is incorrect, and the current operation cannot be executed.");
        }
    }

    private void change(WorkOrderTicket ticket, TicketStateChangeAction action, TicketEvent<Body> event) {
        preChangeInspection(ticket, action, event);
        processing(ticket, action, event);
        setNextState(ticket);
    }

    protected void processing(WorkOrderTicket ticket, TicketStateChangeAction action, TicketEvent<Body> event) {
    }

    @Override
    public void change(WorkOrderTicketParam.HasTicketId hasTicketId, TicketStateChangeAction action,
                       TicketEvent<Body> ticketEvent) {
        WorkOrderTicket ticket = workOrderTicketService.getById(hasTicketId.getTicketId());
        this.change(ticket, action, ticketEvent);
    }

    protected void setNextState(WorkOrderTicket ticket) {
        TicketStateProcessor<Body> processor = (TicketStateProcessor<Body>) TicketInStateProcessorFactory.getByState(
                TicketState.valueOf(ticket.getTicketState()));
        TicketStateProcessor<Body> nextProcessor = getTarget();
        if (Objects.nonNull(nextProcessor)) {
            ticket.setTicketState(nextProcessor.getState()
                    .name());
            workOrderTicketService.updateByPrimaryKey(ticket);
        }
    }

}
