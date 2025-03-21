package com.baiyi.cratos.workorder.state.machine.impl;

import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.workorder.annotation.TicketStates;
import com.baiyi.cratos.workorder.event.TicketEvent;
import com.baiyi.cratos.workorder.state.TicketState;
import com.baiyi.cratos.workorder.state.TicketStateChangeAction;
import com.baiyi.cratos.workorder.state.machine.BaseTicketStateProcessor;
import org.springframework.stereotype.Component;

import static com.baiyi.cratos.workorder.state.TicketStateChangeAction.APPROVAL_APPROVED;
import static com.baiyi.cratos.workorder.state.TicketStateChangeAction.APPROVAL_REJECTED;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/21 11:35
 * &#064;Version 1.0
 */
@Component
@TicketStates(state = TicketState.IN_APPROVAL)
public class TicketInApprovalStateProcessor extends BaseTicketStateProcessor<WorkOrderTicketParam.ApprovalTicket> {

    public TicketInApprovalStateProcessor(WorkOrderTicketService workOrderTicketService) {
        super(workOrderTicketService);
    }

    @Override
    protected void processing(WorkOrderTicket ticket, TicketStateChangeAction action, TicketEvent<WorkOrderTicketParam.ApprovalTicket> event) {
        if (action.equals(APPROVAL_APPROVED)) {
            //  DO_NEXT
        }

        if (action.equals(APPROVAL_REJECTED)) {

        }
    }

    private void changeWithApprovalApproved() {

    }

}
