package com.baiyi.cratos.workorder.entry.impl;

import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.model.ApplicationModel;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.workorder.annotation.WorkOrderKey;
import com.baiyi.cratos.workorder.entry.base.BaseTicketEntryProvider;
import com.baiyi.cratos.workorder.enums.WorkOrderKeys;
import com.baiyi.cratos.workorder.exception.WorkOrderTicketException;
import com.baiyi.cratos.workorder.model.TicketEntryModel;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/6/16 15:55
 * &#064;Version 1.0
 */
@Component
@BusinessType(type = BusinessTypeEnum.APPLICATION)
@WorkOrderKey(key = WorkOrderKeys.APPLICATION_FRONTEND_CREATE)
public class CreateFrontEndApplicationTicketEntryProvider extends BaseTicketEntryProvider<ApplicationModel.CreateFrontEndApplication, WorkOrderTicketParam.AddCreateFrontEndApplicationTicketEntry> {

    public CreateFrontEndApplicationTicketEntryProvider(WorkOrderTicketEntryService workOrderTicketEntryService,
                                                        WorkOrderTicketService workOrderTicketService,
                                                        WorkOrderService workOrderService) {
        super(workOrderTicketEntryService, workOrderTicketService, workOrderService);
    }

    @Override
    protected void processEntry(WorkOrderTicket workOrderTicket, WorkOrderTicketEntry entry,
                                ApplicationModel.CreateFrontEndApplication createFrontEndApplication) throws WorkOrderTicketException {

    }

    @Override
    protected WorkOrderTicketEntry paramToEntry(
            WorkOrderTicketParam.AddCreateFrontEndApplicationTicketEntry addCreateFrontEndApplicationTicketEntry) {
        return null;
    }

    @Override
    public String getTableTitle(WorkOrderTicketEntry entry) {
        return "";
    }

    @Override
    public String getEntryTableRow(WorkOrderTicketEntry entry) {
        return "";
    }

    @Override
    public TicketEntryModel.EntryDesc getEntryDesc(WorkOrderTicketEntry entry) {
        return null;
    }

}
