package com.baiyi.cratos.workorder.entry.impl;

import com.baiyi.cratos.common.exception.WorkOrderTicketException;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.facade.UserPermissionBusinessFacade;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.param.http.user.UserPermissionBusinessParam;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.workorder.builder.entry.ServerAccountPermissionTicketEntryBuilder;
import com.baiyi.cratos.workorder.entry.BaseTicketEntryProvider;
import com.baiyi.cratos.workorder.enums.WorkOrderKeys;
import com.baiyi.cratos.workorder.util.TableUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/19 16:19
 * &#064;Version 1.0
 */
@Component
@BusinessType(type = BusinessTypeEnum.SERVER_ACCOUNT)
public class ServerAccountPermissionTicketEntryProvider extends BaseTicketEntryProvider<UserPermissionBusinessParam.BusinessPermission, WorkOrderTicketParam.AddServerAccountPermissionTicketEntry> {

    private final UserPermissionBusinessFacade userPermissionBusinessFacade;

    public ServerAccountPermissionTicketEntryProvider(WorkOrderTicketEntryService workOrderTicketEntryService,
                                                      WorkOrderTicketService workOrderTicketService,
                                                      UserPermissionBusinessFacade userPermissionBusinessFacade) {
        super(workOrderTicketEntryService, workOrderTicketService);
        this.userPermissionBusinessFacade = userPermissionBusinessFacade;
    }

    @Override
    public String getTableTitle(WorkOrderTicketEntry entry) {
        UserPermissionBusinessParam.BusinessPermission businessPermission = loadAs(entry);
        StringBuilder row = new StringBuilder("| ServerAccount Name |");
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

    @Override
    public String getKey() {
        return WorkOrderKeys.SERVER_ACCOUNT_PERMISSION.name();
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
    protected void processEntry(WorkOrderTicket workOrderTicket, WorkOrderTicketEntry entry,
                                UserPermissionBusinessParam.BusinessPermission businessPermission) throws WorkOrderTicketException {
        UserPermissionBusinessParam.UpdateUserPermissionBusiness updateUserPermissionBusiness = UserPermissionBusinessParam.UpdateUserPermissionBusiness.builder()
                .businessPermissions(List.of(businessPermission))
                .username(workOrderTicket.getUsername())
                .businessType(BusinessTypeEnum.SERVER_ACCOUNT.name())
                .build();
        try {
            // 账户授权
            userPermissionBusinessFacade.updateUserPermissionBusiness(updateUserPermissionBusiness);
        } catch (Exception e) {
            WorkOrderTicketException.runtime(e.getMessage());
        }
    }

    @Override
    public WorkOrderTicketEntry paramToEntry(WorkOrderTicketParam.AddServerAccountPermissionTicketEntry param) {
        return ServerAccountPermissionTicketEntryBuilder.newBuilder()
                .withParam(param)
                .buildEntry();
    }

}
