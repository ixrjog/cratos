package com.baiyi.cratos.workorder.entry.impl.permission;

import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.WorkOrder;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.param.http.user.UserPermissionBusinessParam;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.workorder.annotation.WorkOrderKey;
import com.baiyi.cratos.workorder.builder.entry.ApplicationPermissionTicketEntryBuilder;
import com.baiyi.cratos.workorder.entry.base.BaseTicketEntryProvider;
import com.baiyi.cratos.workorder.enums.WorkOrderKeys;
import com.baiyi.cratos.workorder.exception.WorkOrderTicketException;
import com.baiyi.cratos.workorder.model.TicketEntryModel;
import com.baiyi.cratos.workorder.util.TableUtils;
import com.google.common.base.Joiner;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/5/8 14:14
 * &#064;Version 1.0
 */
@Component
@BusinessType(type = BusinessTypeEnum.EDS_ASSET)
@WorkOrderKey(key = WorkOrderKeys.NEXUS_PERMISSION)
public class NexusPermissionTicketEntryProvider extends BaseTicketEntryProvider<UserPermissionBusinessParam.BusinessPermission, WorkOrderTicketParam.AddApplicationPermissionTicketEntry> {

    public NexusPermissionTicketEntryProvider(WorkOrderTicketEntryService workOrderTicketEntryService,
                                              WorkOrderTicketService workOrderTicketService,
                                              WorkOrderService workOrderService) {
        super(workOrderTicketEntryService, workOrderTicketService, workOrderService);
    }

    private static final String TABLE_TITLE = """
            | Role Name | Desc |
            | --- | --- |
            """;

    private static final String ROW_TPL = "| {} | {} |";

    @Override
    public String getTableTitle(WorkOrderTicketEntry entry) {
       return TABLE_TITLE;
    }

    /**
     * @param entry
     * @return
     */
    @Override
    public String getEntryTableRow(WorkOrderTicketEntry entry) {
        WorkOrder workOrder = getWorkOrder(entry);
        return TableUtils.getBusinessPermissionEntryTableRow(workOrder.getWorkOrderKey(), entry);
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
        // TODO
    }

    @Override
    public WorkOrderTicketEntry paramToEntry(WorkOrderTicketParam.AddApplicationPermissionTicketEntry param) {
        return ApplicationPermissionTicketEntryBuilder.newBuilder()
                .withParam(param)
                .buildEntry();
    }

}
