package com.baiyi.cratos.workorder.state.machine;

import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.workorder.state.TicketState;
import com.baiyi.cratos.workorder.state.TicketStateChangeAction;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/21 11:28
 * &#064;Version 1.0
 */
public interface TicketStateProcessor {

    TicketStateProcessor setTarget(TicketStateProcessor processor);

    TicketStateProcessor getTarget();

    TicketState getState();

    TicketStateProcessor getByState(TicketState ticketState);

    void change(WorkOrderTicket ticket, TicketStateChangeAction action);

}
