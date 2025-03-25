package com.baiyi.cratos.workorder.state.machine.impl;

import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.service.work.WorkOrderTicketNodeService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.workorder.annotation.TicketStates;
import com.baiyi.cratos.workorder.facade.WorkOrderTicketNodeFacade;
import com.baiyi.cratos.workorder.facade.WorkOrderTicketSubscriberFacade;
import com.baiyi.cratos.workorder.state.TicketState;
import com.baiyi.cratos.workorder.state.machine.BaseTicketStateProcessor;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/21 13:44
 * &#064;Version 1.0
 */
@Component
@TicketStates(state = TicketState.APPROVAL_COMPLETED, target = TicketState.IN_PROGRESS)
public class TicketInApprovalCompletedStateProcessor extends BaseTicketStateProcessor<WorkOrderTicketParam.SimpleTicketNo> {

    public TicketInApprovalCompletedStateProcessor(UserService userService, WorkOrderService workOrderService,
                                                   WorkOrderTicketService workOrderTicketService,
                                                   WorkOrderTicketNodeService workOrderTicketNodeService,
                                                   WorkOrderTicketSubscriberFacade workOrderTicketSubscriberFacade,
                                                   WorkOrderTicketNodeFacade workOrderTicketNodeFacade,
                                                   WorkOrderTicketEntryService workOrderTicketEntryService) {
        super(userService, workOrderService, workOrderTicketService, workOrderTicketNodeService,
                workOrderTicketSubscriberFacade, workOrderTicketNodeFacade, workOrderTicketEntryService);
    }

    @Override
    protected boolean isTransition(WorkOrderTicketParam.HasTicketNo hasTicketNo) {
        return true;
    }

}
