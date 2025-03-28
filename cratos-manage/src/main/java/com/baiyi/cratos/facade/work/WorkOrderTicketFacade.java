package com.baiyi.cratos.facade.work;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.domain.view.work.WorkOrderTicketVO;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/19 11:08
 * &#064;Version 1.0
 */
public interface WorkOrderTicketFacade {

    DataTable<WorkOrderTicketVO.Ticket> queryMyTicketPage(WorkOrderTicketParam.MyTicketPageQuery pageQuery);

    WorkOrderTicketVO.TicketDetails createTicket(WorkOrderTicketParam.CreateTicket createTicket);

    WorkOrderTicketVO.TicketDetails getTicket(String ticketNo);

    WorkOrderTicketVO.TicketDetails submitTicket(WorkOrderTicketParam.SubmitTicket submitTicket);

    WorkOrderTicketVO.TicketDetails approvalTicket(WorkOrderTicketParam.ApprovalTicket approvalTicket);

    WorkOrderTicketVO.TicketDetails doNextStateOfTicket(WorkOrderTicketParam.SimpleTicketNo simpleTicketNo);

    /**
     * 逻辑删除，设定为无效工单
     *
     * @param ticketId
     */
    void deleteTicketById(int ticketId);

}
