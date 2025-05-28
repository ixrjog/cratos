package com.baiyi.cratos.workorder.util;

import com.baiyi.cratos.common.util.TimeUtils;
import com.baiyi.cratos.domain.constant.Global;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.param.http.user.UserPermissionBusinessParam;
import com.baiyi.cratos.workorder.entry.TicketEntryProvider;
import com.baiyi.cratos.workorder.entry.TicketEntryProviderFactory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/25 17:00
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TableUtils {

    @SuppressWarnings("DataFlowIssue")
    public static String getBusinessPermissionEntryTableRow(String workOrderKey, WorkOrderTicketEntry entry) {
        TicketEntryProvider<?, ?> provider = TicketEntryProviderFactory.getProvider(workOrderKey,
                entry.getBusinessType());
        UserPermissionBusinessParam.BusinessPermission businessPermission = (UserPermissionBusinessParam.BusinessPermission) provider.loadAs(
                entry);
        StringBuilder row = new StringBuilder("| ").append(entry.getName())
                .append(" |");
        businessPermission.getRoleMembers()
                .forEach(e -> row.append(e.getChecked() ? TimeUtils.parse(e.getExpiredTime(), Global.ISO8601) : " --")
                        .append(" |"));
        return row.toString();
    }

}
