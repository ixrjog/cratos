package com.baiyi.cratos.workorder.entry.impl;

import com.baiyi.cratos.workorder.exception.WorkOrderTicketException;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.facade.UserPermissionBusinessFacade;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.param.http.user.UserPermissionBusinessParam;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.workorder.builder.entry.ApplicationPermissionTicketEntryBuilder;
import com.baiyi.cratos.workorder.entry.BaseTicketEntryProvider;
import com.baiyi.cratos.workorder.enums.WorkOrderKeys;
import com.baiyi.cratos.workorder.model.TicketEntryModel;
import com.baiyi.cratos.workorder.util.TableUtils;
import com.google.common.base.Joiner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/19 13:58
 * &#064;Version 1.0
 */
@Component
@BusinessType(type = BusinessTypeEnum.APPLICATION)
public class ApplicationPermissionTicketEntryProvider extends BaseTicketEntryProvider<UserPermissionBusinessParam.BusinessPermission, WorkOrderTicketParam.AddApplicationPermissionTicketEntry> {

    private final UserPermissionBusinessFacade userPermissionBusinessFacade;

    public ApplicationPermissionTicketEntryProvider(WorkOrderTicketEntryService workOrderTicketEntryService,
                                                    WorkOrderTicketService workOrderTicketService,
                                                    UserPermissionBusinessFacade userPermissionBusinessFacade) {
        super(workOrderTicketEntryService, workOrderTicketService);
        this.userPermissionBusinessFacade = userPermissionBusinessFacade;
    }

    @Override
    public String getKey() {
        return WorkOrderKeys.APPLICATION_PERMISSION.name();
    }

    @Override
    public String getTableTitle(WorkOrderTicketEntry entry) {
        UserPermissionBusinessParam.BusinessPermission businessPermission = loadAs(entry);
        StringBuilder row = new StringBuilder("| Application Name |");
        businessPermission.getRoleMembers()
                .forEach(e -> row.append(" ")
                        .append(e.getRole()
                                .toUpperCase())
                        .append(" |"));
        row.append("\n| --- |");
        businessPermission.getRoleMembers()
                .forEach(e -> row.append("--- |"));
        row.append("\n");
        return row.toString();
    }

    /**
     * @param entry
     * @return
     */
    @Override
    public String getEntryTableRow(WorkOrderTicketEntry entry) {
        return TableUtils.getBusinessPermissionEntryTableRow(entry);
    }

    @Override
    public TicketEntryModel.EntryDesc getEntryDesc(WorkOrderTicketEntry entry) {
        UserPermissionBusinessParam.BusinessPermission businessPermission = loadAs(entry);
        return TicketEntryModel.EntryDesc.builder()
                .name(entry.getName())
                .namespaces(Joiner.on(",")
                        .join(businessPermission.getRoleMembers()
                                .stream()
                                .filter(UserPermissionBusinessParam.RoleMember::getChecked)
                                .toList()))
                .desc("Application permission")
                .build();
    }

    @Override
    protected void processEntry(WorkOrderTicket workOrderTicket, WorkOrderTicketEntry entry,
                                UserPermissionBusinessParam.BusinessPermission businessPermission) throws WorkOrderTicketException {
        UserPermissionBusinessParam.UpdateUserPermissionBusiness updateUserPermissionBusiness = UserPermissionBusinessParam.UpdateUserPermissionBusiness.builder()
                .businessPermissions(List.of(businessPermission))
                .username(workOrderTicket.getUsername())
                .businessType(BusinessTypeEnum.APPLICATION.name())
                .build();
        try {
            userPermissionBusinessFacade.updateUserPermissionBusiness(updateUserPermissionBusiness);
        } catch (Exception e) {
            WorkOrderTicketException.runtime(e.getMessage());
        }
    }

    @Override
    public WorkOrderTicketEntry paramToEntry(WorkOrderTicketParam.AddApplicationPermissionTicketEntry param) {
        return ApplicationPermissionTicketEntryBuilder.newBuilder()
                .withParam(param)
                .buildEntry();
    }

}
