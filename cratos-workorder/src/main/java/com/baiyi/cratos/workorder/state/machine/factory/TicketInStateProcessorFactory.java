package com.baiyi.cratos.workorder.state.machine.factory;

import com.baiyi.cratos.workorder.state.TicketState;
import com.baiyi.cratos.workorder.state.machine.TicketStateProcessor;
import com.baiyi.cratos.workorder.state.machine.impl.TicketNewStateProcessor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/21 14:17
 * &#064;Version 1.0
 */
@SuppressWarnings("rawtypes")
public class TicketInStateProcessorFactory {

    private static TicketStateProcessor context;

    public static void setContext(TicketNewStateProcessor context) {
        TicketInStateProcessorFactory.context = context;
    }

    public static TicketStateProcessor getByState(TicketState ticketState) {
        return context.getByState(ticketState);
    }

}
