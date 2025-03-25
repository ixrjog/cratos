package com.baiyi.cratos.workorder.state.machine.impl;

import com.baiyi.cratos.domain.generator.WorkOrder;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.service.work.WorkOrderTicketNodeService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.workorder.annotation.TicketStates;
import com.baiyi.cratos.workorder.entry.TicketEntryProvider;
import com.baiyi.cratos.workorder.entry.TicketEntryProviderFactory;
import com.baiyi.cratos.workorder.event.TicketEvent;
import com.baiyi.cratos.workorder.facade.WorkOrderTicketNodeFacade;
import com.baiyi.cratos.workorder.facade.WorkOrderTicketSubscriberFacade;
import com.baiyi.cratos.workorder.state.TicketState;
import com.baiyi.cratos.workorder.state.TicketStateChangeAction;
import com.baiyi.cratos.workorder.state.machine.BaseTicketStateProcessor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/21 13:46
 * &#064;Version 1.0
 */
@Component
@TicketStates(state = TicketState.IN_PROGRESS)
public class TicketInProgressStateProcessor extends BaseTicketStateProcessor<WorkOrderTicketParam.SimpleTicketNo> {

    public TicketInProgressStateProcessor(UserService userService, WorkOrderService workOrderService,
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

    @Override
    protected void processing(TicketStateChangeAction action, TicketEvent<WorkOrderTicketParam.SimpleTicketNo> event) {
        WorkOrderTicket ticket = getTicketByNo(event.getBody());
        ticket.setProcessAt(new Date());
        workOrderTicketService.updateByPrimaryKey(ticket);
        WorkOrder workOrder = workOrderService.getById(ticket.getWorkOrderId());
        TicketEntryProvider<?, ?> provider = TicketEntryProviderFactory.getByProvider(workOrder.getWorkOrderKey());
        List<WorkOrderTicketEntry> entries = workOrderTicketEntryService.queryTicketEntries(ticket.getId());
        for (WorkOrderTicketEntry entry : entries) {
            provider.processEntry(entry);
        }
    }

}
