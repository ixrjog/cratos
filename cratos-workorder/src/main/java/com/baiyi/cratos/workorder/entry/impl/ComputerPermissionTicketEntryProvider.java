package com.baiyi.cratos.workorder.entry.impl;

import com.baiyi.cratos.common.enums.RenewalExtUserTypeEnum;
import com.baiyi.cratos.common.enums.SysTagKeys;
import com.baiyi.cratos.common.exception.WorkOrderTicketException;
import com.baiyi.cratos.common.util.ExpiredUtil;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.facade.UserPermissionBusinessFacade;
import com.baiyi.cratos.domain.generator.BusinessTag;
import com.baiyi.cratos.domain.generator.Tag;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.param.http.user.UserPermissionBusinessParam;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.service.BusinessTagService;
import com.baiyi.cratos.service.ServerAccountService;
import com.baiyi.cratos.service.TagService;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.workorder.builder.entry.ComputerPermissionTicketEntryBuilder;
import com.baiyi.cratos.workorder.entry.BaseTicketEntryProvider;
import com.baiyi.cratos.workorder.entry.TicketEntryProvider;
import com.baiyi.cratos.workorder.entry.TicketEntryProviderFactory;
import com.baiyi.cratos.workorder.enums.WorkOrderKeys;
import com.baiyi.cratos.workorder.util.TableUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/19 15:56
 * &#064;Version 1.0
 */
@Component
@BusinessType(type = BusinessTypeEnum.TAG_GROUP)
public class ComputerPermissionTicketEntryProvider extends BaseTicketEntryProvider<UserPermissionBusinessParam.BusinessPermission, WorkOrderTicketParam.AddComputerPermissionTicketEntry> {

    private final UserPermissionBusinessFacade userPermissionBusinessFacade;
    private final BusinessTagService businessTagService;
    private final TagService tagService;
    private final ServerAccountService serverAccountService;

    public ComputerPermissionTicketEntryProvider(WorkOrderTicketEntryService workOrderTicketEntryService,
                                                 WorkOrderTicketService workOrderTicketService,
                                                 UserPermissionBusinessFacade userPermissionBusinessFacade,
                                                 BusinessTagService businessTagService, TagService tagService,
                                                 ServerAccountService serverAccountService) {
        super(workOrderTicketEntryService, workOrderTicketService);
        this.userPermissionBusinessFacade = userPermissionBusinessFacade;
        this.businessTagService = businessTagService;
        this.tagService = tagService;
        this.serverAccountService = serverAccountService;
    }

    @Override
    public String getTableTitle(WorkOrderTicketEntry entry) {
        /**
         * | Group Name |DEV |DAILY |SIT |PRE |PROD |
         * | --- |--- |--- |--- |--- |--- |
         */
        UserPermissionBusinessParam.BusinessPermission businessPermission = loadAs(entry);
        StringBuilder row = new StringBuilder("| Group Name |");
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
    public String getKey() {
        return WorkOrderKeys.COMPUTER_PERMISSION.name();
    }

    @Override
    protected void processEntry(WorkOrderTicket workOrderTicket, WorkOrderTicketEntry entry,
                                UserPermissionBusinessParam.BusinessPermission businessPermission) throws WorkOrderTicketException {
        UserPermissionBusinessParam.UpdateUserPermissionBusiness updateUserPermissionBusiness = UserPermissionBusinessParam.UpdateUserPermissionBusiness.builder()
                .businessPermissions(List.of(businessPermission))
                .username(workOrderTicket.getUsername())
                .businessType(BusinessTypeEnum.TAG_GROUP.name())
                .build();
        try {
            userPermissionBusinessFacade.updateUserPermissionBusiness(updateUserPermissionBusiness);
        } catch (Exception e) {
            WorkOrderTicketException.runtime(e.getMessage());
        }
    }

    @Override
    public WorkOrderTicketEntry paramToEntry(WorkOrderTicketParam.AddComputerPermissionTicketEntry param) {
        return ComputerPermissionTicketEntryBuilder.newBuilder()
                .withParam(param)
                .buildEntry();
    }

    @Override
    public WorkOrderTicketEntry addEntry(WorkOrderTicketParam.AddComputerPermissionTicketEntry param) {
        WorkOrderTicketEntry entry = super.addEntry(param);
        // 给用户授权所有的ServerAccount
        addServerAccounts(param);
        return entry;
    }

    @SuppressWarnings("unchecked")
    private void addServerAccounts(WorkOrderTicketParam.AddComputerPermissionTicketEntry param) {
        try {
            TicketEntryProvider<?, WorkOrderTicketParam.AddServerAccountPermissionTicketEntry> serverAccountProvider = (TicketEntryProvider<?, WorkOrderTicketParam.AddServerAccountPermissionTicketEntry>) TicketEntryProviderFactory.getByProvider(
                    WorkOrderKeys.SERVER_ACCOUNT_PERMISSION.name());
            Tag edsTag = tagService.getByTagKey(SysTagKeys.EDS.getKey());
            if (Objects.isNull(edsTag)) {
                return;
            }
            List<BusinessTag> businessTags = businessTagService.queryByBusinessTypeAndTagId(
                    BusinessTypeEnum.SERVER_ACCOUNT.name(), edsTag.getId());
            if (CollectionUtils.isEmpty(businessTags)) {
                return;
            }
            businessTags.forEach(businessTag -> {
                UserPermissionBusinessParam.BusinessPermission detail = UserPermissionBusinessParam.BusinessPermission.builder()
                        .businessId(businessTag.getBusinessId())
                        .roleMembers(toRoleMembers(param))
                        .name(serverAccountService.getById(businessTag.getBusinessId())
                                .getName())
                        .build();
                WorkOrderTicketParam.AddServerAccountPermissionTicketEntry addServerAccountPermissionTicketEntry = WorkOrderTicketParam.AddServerAccountPermissionTicketEntry.builder()
                        .detail(detail)
                        .ticketId(param.getTicketId())
                        .build();
                serverAccountProvider.addEntry(addServerAccountPermissionTicketEntry);
            });
        } catch (Exception ignored) {
        }
    }

    @Override
    public UserPermissionBusinessParam.BusinessPermission loadAs(WorkOrderTicketEntry entry) {
        if (BusinessTypeEnum.SERVER_ACCOUNT.name()
                .equals(entry.getBusinessType())) {
            return (UserPermissionBusinessParam.BusinessPermission) TicketEntryProviderFactory.getByProvider(
                            WorkOrderKeys.SERVER_ACCOUNT_PERMISSION.name())
                    .loadAs(entry);
        }
        return super.loadAs(entry);
    }

    private List<UserPermissionBusinessParam.RoleMember> toRoleMembers(
            WorkOrderTicketParam.AddComputerPermissionTicketEntry param) {
        return param.getDetail()
                .getRoleMembers()
                .stream()
                .map(e -> UserPermissionBusinessParam.RoleMember.builder()
                        .role(e.getRole())
                        .checked(true)
                        .expiredTime(ExpiredUtil.generateExpirationTime(RenewalExtUserTypeEnum.LONG_TERM.getDays(),
                                TimeUnit.DAYS))
                        .build())
                .toList();
    }

}
