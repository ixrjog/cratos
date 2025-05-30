package com.baiyi.cratos.workorder.builder.entry;

import com.baiyi.cratos.domain.YamlUtils;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/15 09:56
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RevokeUserPermissionTicketEntryBuilder {

    private WorkOrderTicketParam.AddRevokeUserPermissionTicketEntry param;

    public static RevokeUserPermissionTicketEntryBuilder newBuilder() {
        return new RevokeUserPermissionTicketEntryBuilder();
    }

    public RevokeUserPermissionTicketEntryBuilder withParam(WorkOrderTicketParam.AddRevokeUserPermissionTicketEntry param) {
        this.param = param;
        return this;
    }

    public WorkOrderTicketEntry buildEntry() {
        return WorkOrderTicketEntry.builder()
                .ticketId(param.getTicketId())
                .name(param.getDetail()
                        .getUsername())
                .displayName(param.getDetail()
                        .getDisplayName())
                .instanceId(0)
                .businessType(param.getBusinessType())
                .businessId(param.getDetail()
                        .getBusinessId())
                .completed(false)
                .entryKey(param.getDetail()
                        .getUsername())
                .valid(true)
                .content(YamlUtils.dump(param.getDetail()))
                .build();
    }

}
