package com.baiyi.cratos.workorder.util;

import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/20 11:18
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WorkOrderEntryStatusUtils {

    public static void success(WorkOrderTicketEntry entry) {
        entry.setSuccess(true);
        completed(entry);
    }

    public static void failed(WorkOrderTicketEntry entry, String message) {
        entry.setResult(message);
        entry.setSuccess(false);
        completed(entry);
    }

    public static void completed(WorkOrderTicketEntry entry) {
        entry.setExecutedAt(new Date());
        entry.setCompletedAt(new Date());
        entry.setCompleted(true);
    }

}
