package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface WorkOrderTicketMapper extends Mapper<WorkOrderTicket> {

    List<WorkOrderTicket> queryMyTicketPageByParam(WorkOrderTicketParam.MyTicketPageQuery pageQuery);

    List<WorkOrderTicket> queryPageByParam(WorkOrderTicketParam.TicketPageQuery pageQuery);

}