package com.baiyi.cratos.workorder.state.machine.impl;

import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
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
public class TicketInApprovalStateProcessor extends BaseTicketStateProcessor {

    public TicketInApprovalStateProcessor(WorkOrderTicketService workOrderTicketService) {
        super(workOrderTicketService);
    }

    @Override
    public TicketState getState() {
        return TicketState.IN_APPROVAL;
    }

    @Override
    public void change(WorkOrderTicket ticket, TicketStateChangeAction action) {
        if(action.equals(APPROVAL_APPROVED)) {
           //  DO_NEXT
        }

        if(action.equals(APPROVAL_REJECTED)) {

        }
    }

    private void changeWithApprovalApproved() {

    }

}
