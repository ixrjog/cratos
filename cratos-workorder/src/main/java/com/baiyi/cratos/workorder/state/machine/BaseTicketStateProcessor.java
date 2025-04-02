package com.baiyi.cratos.workorder.state.machine;

import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.service.work.WorkOrderTicketNodeService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.workorder.enums.TicketState;
import com.baiyi.cratos.workorder.event.TicketEvent;
import com.baiyi.cratos.workorder.exception.TicketStateProcessorException;
import com.baiyi.cratos.workorder.exception.WorkOrderTicketDoNextException;
import com.baiyi.cratos.workorder.facade.TicketWorkflowFacade;
import com.baiyi.cratos.workorder.facade.WorkOrderTicketNodeFacade;
import com.baiyi.cratos.workorder.facade.WorkOrderTicketSubscriberFacade;
import com.baiyi.cratos.workorder.enums.TicketStateChangeAction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/21 11:30
 * &#064;Version 1.0
 */
@SuppressWarnings("unchecked")
@Slf4j
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
    protected final TicketWorkflowFacade ticketWorkflowFacade;

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

    protected void preChangeInspection(TicketStateChangeAction action, TicketEvent<Event> event) {
        TicketState processorState = getState();
        TicketState currentTicketState = TicketState.valueOf(getTicketByNo(event.getBody()
                .getTicketNo()).getTicketState());
        if (!processorState.equals(currentTicketState)) {
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

    protected abstract boolean nextState(TicketStateChangeAction action, TicketEvent<Event> event);

    protected void processing(TicketStateChangeAction action, TicketEvent<Event> event) {
    }

    protected void changeToTarget(TicketEvent<Event> event) {
        WorkOrderTicketParam.SimpleTicketNo simpleTicketNo = WorkOrderTicketParam.SimpleTicketNo.builder()
                .ticketNo(event.getBody()
                        .getTicketNo())
                .build();
        getTarget().change(TicketStateChangeAction.DO_NEXT, (TicketEvent<Event>) TicketEvent.of(simpleTicketNo));
    }

    @Override
    public void change(TicketStateChangeAction action, TicketEvent<Event> event) {
        try {
            preChangeInspection(action, event);
            processing(action, event);
            if (!isTransition(event.getBody())) {
                return;
            }
            transitionToNextState(event.getBody());
            if (nextState(action, event)) {
                changeToTarget(event);
            }
        } catch (WorkOrderTicketDoNextException ignored) {
        }
    }

    protected void transitionToNextState(WorkOrderTicketParam.HasTicketNo hasTicketNo) {
        TicketStateProcessor<Event> nextProcessor = getTarget();
        if (Objects.nonNull(nextProcessor)) {
            WorkOrderTicket ticket = getTicketByNo(hasTicketNo);
            ticket.setTicketState(nextProcessor.getState()
                    .name());
            workOrderTicketService.updateByPrimaryKey(ticket);
        }
    }

}
