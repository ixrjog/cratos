package com.baiyi.cratos.workorder.state.machine.factory;

import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.workorder.event.TicketEvent;
import com.baiyi.cratos.workorder.enums.TicketState;
import com.baiyi.cratos.workorder.enums.TicketStateChangeAction;
import com.baiyi.cratos.workorder.state.machine.BaseTicketStateProcessor;
import com.baiyi.cratos.workorder.state.machine.TicketStateProcessor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/21 14:17
 * &#064;Version 1.0
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class TicketInStateProcessorFactory<Event extends WorkOrderTicketParam.HasTicketNo> {

    private static TicketStateProcessor context;

    public static <Event extends WorkOrderTicketParam.HasTicketNo> void setContext(
            BaseTicketStateProcessor<Event> context) {
        TicketInStateProcessorFactory.context = context;
    }

    public static TicketStateProcessor getByState(TicketState ticketState) {
        return context.getByState(ticketState);
    }

    public static <Event> void change(TicketState ticketState, TicketStateChangeAction action,
                                      TicketEvent<Event> ticketEvent) {
        getByState(ticketState).change(action, ticketEvent);
    }

}
