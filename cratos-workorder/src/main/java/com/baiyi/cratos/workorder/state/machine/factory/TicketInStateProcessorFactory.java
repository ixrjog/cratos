package com.baiyi.cratos.workorder.state.machine.factory;

import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.workorder.event.TicketEvent;
import com.baiyi.cratos.workorder.state.TicketState;
import com.baiyi.cratos.workorder.state.TicketStateChangeAction;
import com.baiyi.cratos.workorder.state.machine.TicketStateProcessor;
import com.baiyi.cratos.workorder.state.machine.impl.TicketNewStateProcessor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/21 14:17
 * &#064;Version 1.0
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class TicketInStateProcessorFactory<Event> {

    private static TicketStateProcessor context;

    public static void setContext(TicketNewStateProcessor context) {
        TicketInStateProcessorFactory.context = context;
    }

    public static TicketStateProcessor getByState(TicketState ticketState) {
        return context.getByState(ticketState);
    }

    public static <Event> void change(TicketState ticketState, WorkOrderTicketParam.HasTicketNo hasTicketId,
                                      TicketStateChangeAction action, TicketEvent<Event> ticketEvent) {
        getByState(ticketState).change(hasTicketId, action, ticketEvent);
    }

//    public static <Event> void change(TicketStateChangeAction action, TicketEvent<Event> ticketEvent) {
//        context.change(null, action, ticketEvent);
//    }

}
