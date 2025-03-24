package com.baiyi.cratos.workorder.state.machine;

import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.service.work.WorkOrderTicketNodeService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.workorder.event.TicketEvent;
import com.baiyi.cratos.workorder.exception.TicketStateProcessorException;
import com.baiyi.cratos.workorder.facade.WorkOrderTicketNodeFacade;
import com.baiyi.cratos.workorder.facade.WorkOrderTicketSubscriberFacade;
import com.baiyi.cratos.workorder.state.TicketState;
import com.baiyi.cratos.workorder.state.TicketStateChangeAction;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/21 11:30
 * &#064;Version 1.0
 */
@SuppressWarnings("unchecked")
@RequiredArgsConstructor
public abstract class BaseTicketStateProcessor<Event extends WorkOrderTicketParam.HasTicketNo> implements TicketStateProcessor<Event> {

    protected final UserService userService;
    protected final WorkOrderService workOrderService;
    private TicketStateProcessor<Event> targetProcessor;
    protected final WorkOrderTicketService workOrderTicketService;
    protected final WorkOrderTicketNodeService workOrderTicketNodeService;
    protected final WorkOrderTicketSubscriberFacade workOrderTicketSubscriberFacade;
    protected final WorkOrderTicketNodeFacade workOrderTicketNodeFacade;
    protected final WorkOrderTicketEntryService workOrderTicketEntryService;

    @SuppressWarnings("rawtypes")
    @Override
    public TicketStateProcessor setTarget(TicketStateProcessor processor) {
        this.targetProcessor = processor;
        return this.targetProcessor;
    }

    @Override
    public TicketStateProcessor<Event> getByState(TicketState ticketState) {
        return getState().equals(ticketState) ? this : this.targetProcessor.getByState(ticketState);
    }

    @Override
    public TicketStateProcessor<Event> getTarget() {
        return targetProcessor;
    }

    protected void preChangeInspection(TicketStateChangeAction action,
                                       TicketEvent<Event> event) {
        if (getState() != TicketState.valueOf(getTicketByNo(event.getBody().getTicketNo()).getTicketState())) {
            TicketStateProcessorException.runtime(
                    "The work order status is incorrect, and the current operation cannot be executed.");
        }
    }

    protected WorkOrderTicket getTicketByNo(String ticketNo) {
        return workOrderTicketService.getByTicketNo(ticketNo);
    }

    protected WorkOrderTicket getTicketByNo(WorkOrderTicketParam.HasTicketNo hasTicketNo) {
        return workOrderTicketService.getByTicketNo(hasTicketNo.getTicketNo());
    }

    /**
     * 是否转换到下一状态
     *
     * @param hasTicketNo
     * @return
     */
    protected abstract boolean isTransition(WorkOrderTicketParam.HasTicketNo hasTicketNo);

    protected void doNextState(WorkOrderTicket ticket) {
    }


    protected void processing( TicketStateChangeAction action,
                              TicketEvent<Event> event) {
    }

    @Override
    public void change(TicketStateChangeAction action,
                       TicketEvent<Event> event) {

        //WorkOrderTicket ticket = workOrderTicketService.getByTicketNo(hasTicketNo.getTicketNo());
        preChangeInspection( action, event);
        processing(action, event);
        if (isTransition(event.getBody())) {
            transitionToNextState(event.getBody());
        }
    }

    protected void transitionToNextState(WorkOrderTicketParam.HasTicketNo hasTicketNo) {
//        TicketStateProcessor<Event> processor = (TicketStateProcessor<Event>) TicketInStateProcessorFactory.getByState(
//                TicketState.valueOf(ticket.getTicketState()));
        TicketStateProcessor<Event> nextProcessor = getTarget();
        if (Objects.nonNull(nextProcessor)) {
            WorkOrderTicket ticket = getTicketByNo(hasTicketNo);
            ticket.setTicketState(nextProcessor.getState()
                    .name());
            workOrderTicketService.updateByPrimaryKey(ticket);
        }
    }

}
