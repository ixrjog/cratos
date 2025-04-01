package com.baiyi.cratos.service.work;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.mapper.WorkOrderTicketMapper;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;
import com.baiyi.cratos.service.base.SupportBusinessService;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/17 17:45
 * &#064;Version 1.0
 */
public interface WorkOrderTicketService extends BaseUniqueKeyService<WorkOrderTicket, WorkOrderTicketMapper>, SupportBusinessService {

    DataTable<WorkOrderTicket> queryPageByParam(WorkOrderTicketParam.MyTicketPageQuery pageQuery);

    DataTable<WorkOrderTicket> queryPageByParam(WorkOrderTicketParam.TicketPageQuery pageQuery);

    default WorkOrderTicket getByTicketNo(String ticketNo) {
        return getByUniqueKey(WorkOrderTicket.builder()
                .ticketNo(ticketNo)
                .build());
    }

}
