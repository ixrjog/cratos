package com.baiyi.cratos.workorder.state.machine.impl;

import com.baiyi.cratos.common.util.SessionUtils;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.workorder.annotation.TicketStates;
import com.baiyi.cratos.workorder.event.TicketEvent;
import com.baiyi.cratos.workorder.exception.TicketStateProcessorException;
import com.baiyi.cratos.workorder.state.TicketState;
import com.baiyi.cratos.workorder.state.TicketStateChangeAction;
import com.baiyi.cratos.workorder.state.machine.BaseTicketStateProcessor;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/21 11:33
 * &#064;Version 1.0
 */
@Component
@TicketStates(state = TicketState.NEW)
public class TicketNewStateProcessor extends BaseTicketStateProcessor<WorkOrderTicketParam.SubmitTicket> {

    public TicketNewStateProcessor(WorkOrderTicketService workOrderTicketService) {
        super(workOrderTicketService);
    }

    @Override
    protected void preChangeInspection(WorkOrderTicket ticket, TicketStateChangeAction action,
                                       TicketEvent<WorkOrderTicketParam.SubmitTicket> event) {
        // 是否本人提交
        if (!ticket.getUsername()
                .equals(SessionUtils.getUsername())) {
            TicketStateProcessorException.runtime("Non personal submission of work order.");
        }
        super.preChangeInspection(ticket, action, event);
    }

    @Override
    protected void processing(WorkOrderTicket ticket, TicketStateChangeAction action,
                              TicketEvent<WorkOrderTicketParam.SubmitTicket> event) {
        ticket.setSubmittedAt(new Date());
        ticket.setApplyRemark(event.getBody().getApplyRemark());
        workOrderTicketService.updateByPrimaryKey(ticket);
    }

}
