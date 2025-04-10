package com.baiyi.cratos.workorder.state.machine.factory;

import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.workorder.enums.TicketState;
import com.baiyi.cratos.workorder.enums.TicketStateChangeAction;
import com.baiyi.cratos.workorder.event.TicketEvent;
import com.baiyi.cratos.workorder.state.machine.BaseTicketStateProcessor;
import com.baiyi.cratos.workorder.state.machine.TicketStateProcessor;

import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2025/3/21 14:17
 * @Version 1.0
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class TicketInStateProcessorFactory<Event extends WorkOrderTicketParam.HasTicketNo> {

    private static TicketStateProcessor stateProcessor;

    public static <Event extends WorkOrderTicketParam.HasTicketNo> void setStateProcessor(
            BaseTicketStateProcessor<Event> stateProcessor) {
        TicketInStateProcessorFactory.stateProcessor = stateProcessor;
    }

    public static TicketStateProcessor getByState(TicketState ticketState) {
        return Optional.ofNullable(stateProcessor.getByState(ticketState))
                .orElseThrow(() -> new IllegalStateException(
                        "No state processor found for ticket state: " + ticketState));
    }

    public static <Event> void change(TicketState ticketState, TicketStateChangeAction action,
                                      TicketEvent<Event> ticketEvent) {
        getByState(ticketState).change(action, ticketEvent);
    }

}
