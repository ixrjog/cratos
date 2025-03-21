package com.baiyi.cratos.workorder.state.machine.factory;

import com.baiyi.cratos.workorder.state.TicketState;
import com.baiyi.cratos.workorder.state.machine.TicketStateProcessor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/21 14:17
 * &#064;Version 1.0
 */
public class TicketInStateProcessorFactory {

   private static TicketStateProcessor context;

   public static void setContext(TicketStateProcessor context) {
       TicketInStateProcessorFactory.context = context;
   }

    public static TicketStateProcessor getByState(TicketState ticketState) {
        return context.getByState(ticketState);
    }

}
