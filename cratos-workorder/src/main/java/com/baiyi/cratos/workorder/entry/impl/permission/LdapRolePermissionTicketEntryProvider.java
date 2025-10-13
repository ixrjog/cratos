package com.baiyi.cratos.workorder.entry.impl.permission;

import com.baiyi.cratos.common.enums.RenewalExtUserTypeEnum;
import com.baiyi.cratos.common.util.ExpiredUtils;
import com.baiyi.cratos.common.util.MarkdownUtils;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.model.LdapUserGroupModel;
import com.baiyi.cratos.domain.param.http.user.UserPermissionBusinessParam;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.eds.core.config.EdsLdapConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.ldap.repo.LdapGroupRepo;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.workorder.annotation.WorkOrderKey;
import com.baiyi.cratos.workorder.builder.entry.LdapRolePermissionTicketEntryBuilder;
import com.baiyi.cratos.workorder.entry.base.BaseTicketEntryProvider;
import com.baiyi.cratos.workorder.enums.WorkOrderKeys;
import com.baiyi.cratos.workorder.exception.WorkOrderTicketException;
import com.baiyi.cratos.workorder.model.TicketEntryModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.baiyi.cratos.workorder.enums.TableHeaderConstants.LDAP_ROLE_PERMISSION;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/5/26 14:38
 * &#064;Version 1.0
 */
@Slf4j
@Component
@BusinessType(type = BusinessTypeEnum.EDS_ASSET)
@WorkOrderKey(key = WorkOrderKeys.LDAP_ROLE_PERMISSION)
public class LdapRolePermissionTicketEntryProvider extends BaseTicketEntryProvider<LdapUserGroupModel.Role, WorkOrderTicketParam.AddLdapRolePermissionTicketEntry> {

    private final LdapGroupRepo ldapGroupRepo;
    private final EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder;

    public LdapRolePermissionTicketEntryProvider(WorkOrderTicketEntryService workOrderTicketEntryService,
                                                 WorkOrderTicketService workOrderTicketService,
                                                 WorkOrderService workOrderService, LdapGroupRepo ldapGroupRepo,
                                                 EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder) {
        super(workOrderTicketEntryService, workOrderTicketService, workOrderService);
        this.ldapGroupRepo = ldapGroupRepo;
        this.edsInstanceProviderHolderBuilder = edsInstanceProviderHolderBuilder;
    }

    @Override
    public String getTableTitle(WorkOrderTicketEntry entry) {
        return MarkdownUtils.generateMarkdownSeparator(LDAP_ROLE_PERMISSION);
    }

    /**
     * @param entry
     * @return
     */
    @Override
    public String getEntryTableRow(WorkOrderTicketEntry entry) {
        LdapUserGroupModel.Role role = loadAs(entry);
        return MarkdownUtils.generateMarkdownTableRow(role.getGroup(), role.getDescription());
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void processEntry(WorkOrderTicket workOrderTicket, WorkOrderTicketEntry entry,
                                LdapUserGroupModel.Role role) throws WorkOrderTicketException {
        EdsInstanceProviderHolder<EdsLdapConfigModel.Ldap, ?> holder = (EdsInstanceProviderHolder<EdsLdapConfigModel.Ldap, ?>) edsInstanceProviderHolderBuilder.newHolder(
                entry.getInstanceId(), EdsAssetTypeEnum.LDAP_GROUP.name());
        EdsLdapConfigModel.Ldap ldap = holder.getInstance()
                .getEdsConfigModel();
        ldapGroupRepo.addGroupMember(ldap, role.getGroup(), workOrderTicket.getUsername());
    }

    @Override
    protected WorkOrderTicketEntry paramToEntry(
            WorkOrderTicketParam.AddLdapRolePermissionTicketEntry addLdapRolePermissionTicketEntry) {
        return LdapRolePermissionTicketEntryBuilder.newBuilder()
                .withParam(addLdapRolePermissionTicketEntry)
                .buildEntry();
    }

    private List<UserPermissionBusinessParam.RoleMember> toRoleMembers(
            WorkOrderTicketParam.AddComputerPermissionTicketEntry param) {
        return param.getDetail()
                .getRoleMembers()
                .stream()
                .map(e -> UserPermissionBusinessParam.RoleMember.builder()
                        .role(e.getRole())
                        .checked(true)
                        .expiredTime(ExpiredUtils.generateExpirationTime(RenewalExtUserTypeEnum.LONG_TERM.getDays(),
                                TimeUnit.DAYS))
                        .build())
                .toList();
    }

    @Override
    public TicketEntryModel.EntryDesc getEntryDesc(WorkOrderTicketEntry entry) {
        LdapUserGroupModel.Role role = loadAs(entry);
        return TicketEntryModel.EntryDesc.builder()
                .name(role.getGroup())
                .namespaces(entry.getNamespace())
                .desc("LDAP Role Permission")
                .build();
    }

}
