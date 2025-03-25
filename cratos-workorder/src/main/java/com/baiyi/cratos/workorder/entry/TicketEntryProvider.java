package com.baiyi.cratos.workorder.entry;

import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/19 13:48
 * &#064;Version 1.0
 */
public interface TicketEntryProvider<Detail, EntryParam extends WorkOrderTicketParam.TicketEntry> extends BaseBusiness.IBusinessTypeAnnotate {

    String getKey();

    WorkOrderTicketEntry addEntry(EntryParam param);

    /**
     * 处理工单条目
     *
     * @param entry
     */
    void processEntry(WorkOrderTicketEntry entry);

    Detail loadAs(WorkOrderTicketEntry entry);

    String getTableTitle(WorkOrderTicketEntry entry);

    String getEntryTableRow(WorkOrderTicketEntry entry);

}
