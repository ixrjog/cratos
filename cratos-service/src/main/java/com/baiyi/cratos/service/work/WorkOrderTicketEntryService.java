package com.baiyi.cratos.service.work;

import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.mapper.WorkOrderTicketEntryMapper;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;
import com.baiyi.cratos.service.base.BaseValidService;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/19 10:45
 * &#064;Version 1.0
 */
public interface WorkOrderTicketEntryService extends BaseValidService<WorkOrderTicketEntry, WorkOrderTicketEntryMapper>, BaseUniqueKeyService<WorkOrderTicketEntry, WorkOrderTicketEntryMapper> {

    List<WorkOrderTicketEntry> queryTicketEntries(int ticketId);

    List<WorkOrderTicketEntry> queryTicketEntries(int ticketId, String businessType);

    int countByTicketId(int ticketId);

}
