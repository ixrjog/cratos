package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.Report;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface WorkOrderTicketMapper extends Mapper<WorkOrderTicket> {

    List<WorkOrderTicket> queryMyTicketPageByParam(WorkOrderTicketParam.MyTicketPageQuery pageQuery);

    List<WorkOrderTicket> queryPageByParam(WorkOrderTicketParam.TicketPageQuery pageQuery);

    List<Report.BaseData> statByMonth(Integer workOrderId);

    List<Report.BaseData> statByName();

}