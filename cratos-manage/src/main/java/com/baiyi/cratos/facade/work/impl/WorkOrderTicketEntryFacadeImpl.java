package com.baiyi.cratos.facade.work.impl;

import com.baiyi.cratos.common.util.SessionUtils;
import com.baiyi.cratos.domain.generator.WorkOrder;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.generator.WorkOrderTicketNode;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.facade.work.WorkOrderTicketEntryFacade;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.service.work.WorkOrderTicketNodeService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.workorder.entry.TicketEntryProvider;
import com.baiyi.cratos.workorder.entry.TicketEntryProviderFactory;
import com.baiyi.cratos.workorder.enums.WorkOrderKeys;
import com.baiyi.cratos.workorder.exception.WorkOrderTicketException;
import com.baiyi.cratos.workorder.facade.TicketWorkflowFacade;
import com.baiyi.cratos.workorder.enums.TicketState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/19 15:33
 * &#064;Version 1.0
 */
@SuppressWarnings("unchecked")
@Component
@RequiredArgsConstructor
public class WorkOrderTicketEntryFacadeImpl implements WorkOrderTicketEntryFacade {

    private final WorkOrderService workOrderService;
    private final WorkOrderTicketService workOrderTicketService;
    private final WorkOrderTicketNodeService workOrderTicketNodeService;
    private final WorkOrderTicketEntryService workOrderTicketEntryService;
    private final TicketWorkflowFacade workflowFacade;

    @Override
    public void addApplicationPermissionTicketEntry(
            WorkOrderTicketParam.AddApplicationPermissionTicketEntry addTicketEntry) {
        TicketEntryProvider<?, WorkOrderTicketParam.AddApplicationPermissionTicketEntry> ticketEntryProvider = (TicketEntryProvider<?, WorkOrderTicketParam.AddApplicationPermissionTicketEntry>) TicketEntryProviderFactory.getByProvider(
                WorkOrderKeys.APPLICATION_PERMISSION.name());
        WorkOrderTicketEntry entry = ticketEntryProvider.addEntry(addTicketEntry);
    }

    @Override
    public void addComputerPermissionTicketEntry(WorkOrderTicketParam.AddComputerPermissionTicketEntry addTicketEntry) {
        TicketEntryProvider<?, WorkOrderTicketParam.AddComputerPermissionTicketEntry> ticketEntryProvider = (TicketEntryProvider<?, WorkOrderTicketParam.AddComputerPermissionTicketEntry>) TicketEntryProviderFactory.getByProvider(
                WorkOrderKeys.COMPUTER_PERMISSION.name());
        WorkOrderTicketEntry entry = ticketEntryProvider.addEntry(addTicketEntry);
    }

    @Override
    public void setValidById(int id) {
        WorkOrderTicketEntry workOrderTicketEntry = workOrderTicketEntryService.getById(id);
        if (Objects.isNull(workOrderTicketEntry)) {
            return;
        }
        WorkOrderTicket workOrderTicket = workOrderTicketService.getById(workOrderTicketEntry.getTicketId());
        if (!TicketState.IN_APPROVAL.equals(TicketState.valueOf(workOrderTicket.getTicketState()))) {
            WorkOrderTicketException.runtime("Only the in approval status can be set as valid.");
        }
        WorkOrder workOrder = workOrderService.getById(workOrderTicket.getWorkOrderId());
        WorkOrderTicketNode workOrderTicketNode = workOrderTicketNodeService.getById(workOrderTicket.getNodeId());
        if (!workflowFacade.isApprover(workOrder, workOrderTicketNode.getNodeName(), SessionUtils.getUsername())) {
            WorkOrderTicketException.runtime("Only the approver of the current node can set valid settings.");
        }
        workOrderTicketEntryService.updateValidById(id);
    }

    @Override
    public void deleteTicketEntry(WorkOrderTicketParam.DeleteTicketEntry deleteTicketEntry) {
        WorkOrderTicketEntry workOrderTicketEntry = workOrderTicketEntryService.getByUniqueKey(
                deleteTicketEntry.totEntryUniqueKey());
        if (Objects.nonNull(workOrderTicketEntry)) {
            deleteById(workOrderTicketEntry.getId());
        }
    }

    @Override
    public void deleteById(int id) {
        WorkOrderTicketEntry workOrderTicketEntry = workOrderTicketEntryService.getById(id);
        if (Objects.isNull(workOrderTicketEntry)) {
            return;
        }
        WorkOrderTicket workOrderTicket = workOrderTicketService.getById(workOrderTicketEntry.getTicketId());
        if (!workOrderTicket.getUsername()
                .equals(SessionUtils.getUsername())) {
            WorkOrderTicketException.runtime("Only the applicant can delete the entry");
        }
        if (!TicketState.NEW.equals(TicketState.valueOf(workOrderTicket.getTicketState()))) {
            WorkOrderTicketException.runtime("Only in the new state can it be deleted.");
        }
        workOrderTicketEntryService.deleteById(id);
    }

}
