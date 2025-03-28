package com.baiyi.cratos.workorder.state.machine.impl;

import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.service.work.WorkOrderTicketNodeService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.workorder.annotation.TicketStates;
import com.baiyi.cratos.workorder.event.TicketEvent;
import com.baiyi.cratos.workorder.facade.WorkOrderTicketNodeFacade;
import com.baiyi.cratos.workorder.facade.WorkOrderTicketSubscriberFacade;
import com.baiyi.cratos.workorder.enums.TicketState;
import com.baiyi.cratos.workorder.state.TicketStateChangeAction;
import com.baiyi.cratos.workorder.state.machine.BaseTicketStateProcessor;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/21 11:34
 * &#064;Version 1.0
 */
@Component
@TicketStates(state = TicketState.SUBMITTED, target = TicketState.IN_APPROVAL)
public class TicketSubmittedStateProcessor extends BaseTicketStateProcessor<WorkOrderTicketParam.SimpleTicketNo> {

    public TicketSubmittedStateProcessor(UserService userService, WorkOrderService workOrderService,
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

    protected boolean nextState() {
        return true;
    }

    @Override
    protected void processing(TicketStateChangeAction action, TicketEvent<WorkOrderTicketParam.SimpleTicketNo> event) {
        // 发送通知
    }

}

