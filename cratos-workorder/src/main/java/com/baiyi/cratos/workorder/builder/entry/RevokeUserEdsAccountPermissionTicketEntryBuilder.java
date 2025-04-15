package com.baiyi.cratos.workorder.builder.entry;

import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/15 11:33
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RevokeUserEdsAccountPermissionTicketEntryBuilder {

    private WorkOrderTicketParam.AddRevokeUserEdsAccountPermissionTicketEntry param;

    public static RevokeUserEdsAccountPermissionTicketEntryBuilder newBuilder() {
        return new RevokeUserEdsAccountPermissionTicketEntryBuilder();
    }

    public RevokeUserEdsAccountPermissionTicketEntryBuilder withParam(WorkOrderTicketParam.AddRevokeUserEdsAccountPermissionTicketEntry param) {
        this.param = param;
        return this;
    }

//    public WorkOrderTicketEntry buildEntry() {
//        return WorkOrderTicketEntry.builder()
//                .ticketId(param.getTicketId())
//                .name(param.getDetail()
//                        .getUsername())
//                .displayName(param.getDetail()
//                        .getDisplayName())
//                .instanceId(0)
//                .businessType(param.getBusinessType())
//                .businessId(param.getDetail()
//                        .getBusinessId())
//                .completed(false)
//                .entryKey(param.getDetail()
//                        .getUsername())
//                .valid(true)
//                .content(YamlUtils.dump(param.getDetail()))
//                .build();
//    }
}
