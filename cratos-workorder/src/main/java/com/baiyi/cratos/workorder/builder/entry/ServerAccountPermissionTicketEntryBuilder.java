package com.baiyi.cratos.workorder.builder.entry;

import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/19 17:27
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ServerAccountPermissionTicketEntryBuilder {

    private WorkOrderTicketParam.AddServerAccountPermissionTicketEntry param;

    public static ServerAccountPermissionTicketEntryBuilder newBuilder() {
        return new ServerAccountPermissionTicketEntryBuilder();
    }

    public ServerAccountPermissionTicketEntryBuilder withParam(
            WorkOrderTicketParam.AddServerAccountPermissionTicketEntry param) {
        this.param = param;
        return this;
    }

    public WorkOrderTicketEntry buildEntry() {
        return WorkOrderTicketEntry.builder()
                .ticketId(param.getTicketId())
                .name(param.getDetail()
                        .getName())
                .displayName(param.getDetail()
                        .getName())
                .instanceId(0)
                .businessType(param.getBusinessType())
                .businessId(param.getDetail()
                        .getBusinessId())
                .completed(false)
                .entryKey(param.getDetail()
                        .getName())
                .valid(true)
                .content(param.getDetail()
                        .dump())
                .build();
    }

}
