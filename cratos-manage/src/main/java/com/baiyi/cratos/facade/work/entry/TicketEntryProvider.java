package com.baiyi.cratos.facade.work.entry;

import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/19 13:48
 * &#064;Version 1.0
 */
public interface TicketEntryProvider<E extends WorkOrderTicketParam.TicketEntry> {

    String getKey();

    WorkOrderTicketEntry addEntry(E param);

    /**
     * 处理工单条目
     * @param entry
     */
    void processEntry(WorkOrderTicketEntry entry);

}
