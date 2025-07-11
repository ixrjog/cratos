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
import com.baiyi.cratos.workorder.enums.TicketState;
import com.baiyi.cratos.workorder.event.TicketEvent;
import com.baiyi.cratos.workorder.facade.TicketWorkflowFacade;
import com.baiyi.cratos.workorder.facade.WorkOrderTicketNodeFacade;
import com.baiyi.cratos.workorder.facade.WorkOrderTicketSubscriberFacade;
import com.baiyi.cratos.workorder.notice.WorkOrderCompletionNoticeSender;
import com.baiyi.cratos.workorder.enums.TicketStateChangeAction;
import com.baiyi.cratos.workorder.state.machine.BaseTicketStateProcessor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/21 13:47
 * &#064;Version 1.0
 */
@Component
@TicketStates(state = TicketState.PROCESSING_COMPLETED, target = TicketState.COMPLETED)
public class TicketProcessingCompletedStateProcessor extends BaseTicketStateProcessor<WorkOrderTicketParam.SimpleTicketNo> {

    private final WorkOrderCompletionNoticeSender workOrderCompletionNoticeSender;

    public TicketProcessingCompletedStateProcessor(UserService userService, WorkOrderService workOrderService,
                                                   WorkOrderTicketService workOrderTicketService,
                                                   WorkOrderTicketNodeService workOrderTicketNodeService,
                                                   WorkOrderTicketSubscriberFacade workOrderTicketSubscriberFacade,
                                                   WorkOrderTicketNodeFacade workOrderTicketNodeFacade,
                                                   WorkOrderTicketEntryService workOrderTicketEntryService,
                                                   TicketWorkflowFacade ticketWorkflowFacade,
                                                   WorkOrderCompletionNoticeSender applicantNotificationHelper) {
        super(userService, workOrderService, workOrderTicketService, workOrderTicketNodeService,
                workOrderTicketSubscriberFacade, workOrderTicketNodeFacade, workOrderTicketEntryService,
                ticketWorkflowFacade);
        this.workOrderCompletionNoticeSender = applicantNotificationHelper;
    }

    @Override
    protected boolean isTransition(WorkOrderTicketParam.HasTicketNo hasTicketNo) {
        return true;
    }

    protected boolean nextState(TicketStateChangeAction action,
                                TicketEvent<WorkOrderTicketParam.SimpleTicketNo> event) {
        return true;
    }

    @Override
    protected void processing(TicketStateChangeAction action, TicketEvent<WorkOrderTicketParam.SimpleTicketNo> event) {
        // 设置工单success
        WorkOrderTicket ticket = getTicketByNo(event.getBody());
        List<WorkOrderTicketEntry> entries = workOrderTicketEntryService.queryTicketEntries(ticket.getId());
        boolean success = entries.stream()
                .allMatch(e -> Boolean.TRUE.equals(e.getSuccess()));
        ticket.setSuccess(success);
        workOrderTicketService.updateByPrimaryKey(ticket);
        // 工单处理完成通知
        WorkOrder workOrder = workOrderService.getById(ticket.getWorkOrderId());
        workOrderCompletionNoticeSender.sendMsg(workOrder, ticket);
    }

}
