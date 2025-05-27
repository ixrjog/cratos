package com.baiyi.cratos.workorder.builder.entry;

import com.baiyi.cratos.domain.YamlUtils;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.model.LdapUserGroupModel;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/5/27 10:27
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LdapRolePermissionTicketEntryBuilder {

    private WorkOrderTicketParam.AddLdapRolePermissionTicketEntry param;

    public static LdapRolePermissionTicketEntryBuilder newBuilder() {
        return new LdapRolePermissionTicketEntryBuilder();
    }

    public LdapRolePermissionTicketEntryBuilder withParam(WorkOrderTicketParam.AddLdapRolePermissionTicketEntry param) {
        this.param = param;
        return this;
    }

    public WorkOrderTicketEntry buildEntry() {
        LdapUserGroupModel.Role role = param.getDetail();
        return WorkOrderTicketEntry.builder()
                .ticketId(param.getTicketId())
                .name(role.getGroup())
                .displayName(role.getGroup())
                .instanceId(role.getAsset()
                        .getInstanceId())
                .businessType(BusinessTypeEnum.EDS_ASSET.name())
                .subType(EdsAssetTypeEnum.LDAP_GROUP.name())
                .businessId(role.getAsset()
                        .getId())
                .completed(false)
                .entryKey(role.getGroup())
                .valid(true)
                .content(YamlUtils.dump(param.getDetail()))
                .comment(role.getDescription())
                .build();
    }

}