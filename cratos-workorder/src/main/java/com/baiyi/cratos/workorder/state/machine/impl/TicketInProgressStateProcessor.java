package com.baiyi.cratos.workorder.state.machine.impl;

import com.baiyi.cratos.domain.generator.WorkOrder;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.generator.WorkOrderTicketNode;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.service.work.WorkOrderTicketNodeService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.workorder.annotation.TicketStates;
import com.baiyi.cratos.workorder.entry.TicketEntryProvider;
import com.baiyi.cratos.workorder.entry.TicketEntryProviderFactory;
import com.baiyi.cratos.workorder.enums.ApprovalStatus;
import com.baiyi.cratos.workorder.enums.ApprovalTypes;
import com.baiyi.cratos.workorder.enums.TicketState;
import com.baiyi.cratos.workorder.enums.TicketStateChangeAction;
import com.baiyi.cratos.workorder.event.TicketEvent;
import com.baiyi.cratos.workorder.facade.TicketWorkflowFacade;
import com.baiyi.cratos.workorder.facade.WorkOrderTicketNodeFacade;
import com.baiyi.cratos.workorder.facade.WorkOrderTicketSubscriberFacade;
import com.baiyi.cratos.workorder.state.machine.BaseTicketStateProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/21 13:46
 * &#064;Version 1.0
 */
@Component
@TicketStates(state = TicketState.IN_PROGRESS, target = TicketState.PROCESSING_COMPLETED)
public class TicketInProgressStateProcessor extends BaseTicketStateProcessor<WorkOrderTicketParam.SimpleTicketNo> {

    public TicketInProgressStateProcessor(UserService userService, WorkOrderService workOrderService,
                                          WorkOrderTicketService workOrderTicketService,
                                          WorkOrderTicketNodeService workOrderTicketNodeService,
                                          WorkOrderTicketSubscriberFacade workOrderTicketSubscriberFacade,
                                          WorkOrderTicketNodeFacade workOrderTicketNodeFacade,
                                          WorkOrderTicketEntryService workOrderTicketEntryService,
                                          TicketWorkflowFacade ticketWorkflowFacade) {
        super(userService, workOrderService, workOrderTicketService, workOrderTicketNodeService,
                workOrderTicketSubscriberFacade, workOrderTicketNodeFacade, workOrderTicketEntryService,
                ticketWorkflowFacade);
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
        WorkOrderTicket ticket = getTicketByNo(event.getBody());
        ticket.setProcessAt(new Date());
        workOrderTicketService.updateByPrimaryKey(ticket);
        WorkOrder workOrder = workOrderService.getById(ticket.getWorkOrderId());
        // 判断是否审批通过
        boolean pass = passApproval(ticket);
        List<WorkOrderTicketEntry> entries = workOrderTicketEntryService.queryTicketEntries(ticket.getId());
        entries.forEach(entry -> {
            if (pass) {
                TicketEntryProvider<?, ?> provider = TicketEntryProviderFactory.getProvider(workOrder.getWorkOrderKey(),
                        entry.getBusinessType());
                if (provider == null) {
                    processFailed(entry, "No provider found for business type: " + entry.getBusinessType());
                } else {
                    provider.processEntry(entry);
                }
            } else {
                processFailed(entry, "Approval rejected");
            }
        });
    }

    private void processFailed(WorkOrderTicketEntry entry, String message) {
        entry.setCompleted(true);
        entry.setCompletedAt(new Date());
        entry.setSuccess(false);
        entry.setResult("Approval rejected");
        workOrderTicketEntryService.updateByPrimaryKey(entry);
    }

    private boolean passApproval(WorkOrderTicket ticket) {
        List<WorkOrderTicketNode> ticketNodes = workOrderTicketNodeService.queryByTicketId(ticket.getId());
        if (CollectionUtils.isEmpty(ticketNodes)) {
            return true;
        }
        return ticketNodes.stream()
                .filter(e -> !ApprovalTypes.CC_TO.equals(ApprovalTypes.valueOf(e.getApprovalType())))
                .allMatch(e -> ApprovalStatus.AGREE.equals(ApprovalStatus.valueOf(e.getApprovalStatus())));
    }

}
