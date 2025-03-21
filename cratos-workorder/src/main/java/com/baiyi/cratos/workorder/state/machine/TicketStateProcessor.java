package com.baiyi.cratos.workorder.state.machine;

import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.workorder.event.TicketEvent;
import com.baiyi.cratos.workorder.state.HasTicketStateAnnotate;
import com.baiyi.cratos.workorder.state.TicketState;
import com.baiyi.cratos.workorder.state.TicketStateChangeAction;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/21 11:28
 * &#064;Version 1.0
 */
@SuppressWarnings("rawtypes")
public interface TicketStateProcessor<Event> extends HasTicketStateAnnotate {

    TicketStateProcessor<Event> setTarget(TicketStateProcessor<Event> processor);

    TicketStateProcessor getTarget();

    TicketStateProcessor getByState(TicketState ticketState);

    void change(WorkOrderTicketParam.HasTicketId hasTicketId, TicketStateChangeAction action,
                TicketEvent<Event> ticketEvent);

}
