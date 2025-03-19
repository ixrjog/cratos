package com.baiyi.cratos.facade.work.entry.impl;

import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.param.http.user.UserPermissionBusinessParam;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.facade.work.builder.entry.ApplicationPermissionTicketEntryBuilder;
import com.baiyi.cratos.facade.work.entry.BaseTicketEntryProvider;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.workorder.enums.WorkOrderKeys;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/19 13:58
 * &#064;Version 1.0
 */
@Component
public class ApplicationPermissionTicketEntryProvider extends BaseTicketEntryProvider<UserPermissionBusinessParam.BusinessPermission, WorkOrderTicketParam.AddApplicationPermissionTicketEntry> {

    public ApplicationPermissionTicketEntryProvider(WorkOrderTicketEntryService workOrderTicketEntryService,
                                                    WorkOrderTicketService workOrderTicketService) {
        super(workOrderTicketEntryService, workOrderTicketService);
    }

    @Override
    public String getKey() {
        return WorkOrderKeys.APPLICATION_PERMISSION.name();
    }

    @Override
    protected void processEntry(WorkOrderTicket workOrderTicket, WorkOrderTicketEntry entry,
                                UserPermissionBusinessParam.BusinessPermission businessPermission) {

        UserPermissionBusinessParam.UpdateUserPermissionBusiness updateUserPermissionBusiness =  UserPermissionBusinessParam.UpdateUserPermissionBusiness.builder()
                .businessPermissions(List.of(businessPermission))
                .username(workOrderTicket.getUsername())
                .businessType(BusinessTypeEnum.APPLICATION.name())
                .build();


    }

    @Override
    public WorkOrderTicketEntry paramToEntry(WorkOrderTicketParam.AddApplicationPermissionTicketEntry param) {
        return ApplicationPermissionTicketEntryBuilder.newBuilder()
                .withParam(param)
                .buildEntry();
    }

}
