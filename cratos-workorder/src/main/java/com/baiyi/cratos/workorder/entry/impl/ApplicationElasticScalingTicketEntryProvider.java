package com.baiyi.cratos.workorder.entry.impl;

import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.param.http.user.UserPermissionBusinessParam;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.workorder.builder.entry.ApplicationPermissionTicketEntryBuilder;
import com.baiyi.cratos.workorder.entry.BaseTicketEntryProvider;
import com.baiyi.cratos.workorder.enums.WorkOrderKeys;
import com.baiyi.cratos.workorder.exception.WorkOrderTicketException;
import com.baiyi.cratos.workorder.model.TicketEntryModel;
import com.baiyi.cratos.workorder.util.TableUtils;
import com.google.common.base.Joiner;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/9 13:34
 * &#064;Version 1.0
 */
@Component
@BusinessType(type = BusinessTypeEnum.APPLICATION)
public class ApplicationElasticScalingTicketEntryProvider extends BaseTicketEntryProvider<UserPermissionBusinessParam.BusinessPermission, WorkOrderTicketParam.AddApplicationPermissionTicketEntry> {

    public ApplicationElasticScalingTicketEntryProvider(WorkOrderTicketEntryService workOrderTicketEntryService,
                                                        WorkOrderTicketService workOrderTicketService) {
        super(workOrderTicketEntryService, workOrderTicketService);
    }

    @Override
    public String getKey() {
        return WorkOrderKeys.APPLICATION_ELASTIC_SCALING.name();
    }

    @Override
    public String getTableTitle(WorkOrderTicketEntry entry) {
        return """
                | Application Name | Namespace | Current Replicas | Expected Replicas |
                | --- | --- | --- | --- |
                """;
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
        String namespaces = Joiner.on(",")
                .join(businessPermission.getRoleMembers()
                        .stream()
                        .filter(UserPermissionBusinessParam.RoleMember::getChecked)
                        .map(UserPermissionBusinessParam.RoleMember::getRole)
                        .toList());
        return TicketEntryModel.EntryDesc.builder()
                .name(entry.getName())
                .namespaces(namespaces)
                .desc("Application permission")
                .build();
    }

    @Override
    protected void processEntry(WorkOrderTicket workOrderTicket, WorkOrderTicketEntry entry,
                                UserPermissionBusinessParam.BusinessPermission businessPermission) throws WorkOrderTicketException {
        // TODO: Implement the logic to process the entry
    }

    @Override
    public WorkOrderTicketEntry paramToEntry(WorkOrderTicketParam.AddApplicationPermissionTicketEntry param) {
        return ApplicationPermissionTicketEntryBuilder.newBuilder()
                .withParam(param)
                .buildEntry();
    }

}
