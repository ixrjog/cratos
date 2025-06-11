package com.baiyi.cratos.workorder.entry.impl.permission.application;

import com.baiyi.cratos.common.util.ExpiredUtil;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.facade.UserPermissionBusinessFacade;
import com.baiyi.cratos.domain.generator.Env;
import com.baiyi.cratos.domain.generator.WorkOrder;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.param.http.user.UserPermissionBusinessParam;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.facade.EnvFacade;
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

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/6/10 15:11
 * &#064;Version 1.0
 */
@Component
@BusinessType(type = BusinessTypeEnum.APPLICATION)
@WorkOrderKey(key = WorkOrderKeys.APPLICATION_PROD_PERMISSION)
public class ApplicationProdPermissionTicketEntryProvider extends BaseTicketEntryProvider<UserPermissionBusinessParam.BusinessPermission, WorkOrderTicketParam.AddApplicationPermissionTicketEntry> {

    private final UserPermissionBusinessFacade userPermissionBusinessFacade;
    private final EnvFacade envFacade;

    private static final String GROUP_VALUE = "prod";

    public ApplicationProdPermissionTicketEntryProvider(WorkOrderTicketEntryService workOrderTicketEntryService,
                                                        WorkOrderTicketService workOrderTicketService,
                                                        WorkOrderService workOrderService,
                                                        UserPermissionBusinessFacade userPermissionBusinessFacade,

                                                        EnvFacade envFacade) {
        super(workOrderTicketEntryService, workOrderTicketService, workOrderService);
        this.userPermissionBusinessFacade = userPermissionBusinessFacade;
        this.envFacade = envFacade;
    }

    @Override
    public String getTableTitle(WorkOrderTicketEntry entry) {
        List<Env> envs = queryEnv(GROUP_VALUE);
        StringBuilder row = new StringBuilder("| Group Name |");
        envs.forEach(e -> row.append(" ")
                .append(e.getEnvName()
                        .toUpperCase())
                .append(" |"));
        row.append("\n| --- |");
        envs.forEach(e -> row.append("--- |"));
        row.append("\n");
        return row.toString();
    }

    @SuppressWarnings("SameParameterValue")
    private List<Env> queryEnv(String groupValue) {
        return envFacade.queryEnv(groupValue);
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
        List<Env> envs = queryEnv(GROUP_VALUE);
        UserPermissionBusinessParam.BusinessPermission businessPermission = loadAs(entry);
        String namespaces = Joiner.on(",")
                .join(businessPermission.getRoleMembers()
                        .stream()
                        .filter(UserPermissionBusinessParam.RoleMember::getChecked)
                        .map(UserPermissionBusinessParam.RoleMember::getRole)
                        .filter(role -> envs.stream()
                                .anyMatch(env -> env.getEnvName()
                                        .equalsIgnoreCase(role)))
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
        List<Env> envs = queryEnv(GROUP_VALUE);
        UserPermissionBusinessParam.BusinessPermission prodBusinessPermission = UserPermissionBusinessParam.BusinessPermission.builder()
                .businessId(businessPermission.getBusinessId())
                .name(businessPermission.getName())
                .roleMembers(businessPermission.getRoleMembers()
                        .stream()
                        .filter(roleMember -> envs.stream()
                                .anyMatch(env -> env.getEnvName()
                                        .equalsIgnoreCase(roleMember.getRole())))
                        .map(roleMember -> UserPermissionBusinessParam.RoleMember.builder()
                                .checked(roleMember.getChecked())
                                .role(roleMember.getRole())
                                .role(roleMember.getRole())
                                .expiredTime(ExpiredUtil.generateExpirationTime(15L, TimeUnit.MINUTES))
                                .build())
                        .toList())
                .build();
        UserPermissionBusinessParam.UpdateUserPermissionBusiness updateUserPermissionBusiness = UserPermissionBusinessParam.UpdateUserPermissionBusiness.builder()
                .businessPermissions(List.of(prodBusinessPermission))
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
