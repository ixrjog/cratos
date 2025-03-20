package com.baiyi.cratos.facade.work.impl;

import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.facade.work.WorkOrderTicketEntryFacade;
import com.baiyi.cratos.facade.work.entry.TicketEntryProvider;
import com.baiyi.cratos.facade.work.entry.TicketEntryProviderFactory;
import com.baiyi.cratos.workorder.enums.WorkOrderKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/19 15:33
 * &#064;Version 1.0
 */
@SuppressWarnings("unchecked")
@Component
@RequiredArgsConstructor
public class WorkOrderTicketEntryFacadeImpl implements WorkOrderTicketEntryFacade {

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

}
