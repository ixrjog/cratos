package com.baiyi.cratos.facade.work.impl;

import com.baiyi.cratos.annotation.SetSessionUserToParam;
import com.baiyi.cratos.common.util.PasswordGenerator;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.domain.view.work.WorkOrderTicketVO;
import com.baiyi.cratos.facade.work.WorkOrderTicketFacade;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.service.work.WorkOrderTicketNodeService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.workorder.event.TicketEvent;
import com.baiyi.cratos.workorder.facade.WorkOrderTicketNodeFacade;
import com.baiyi.cratos.workorder.facade.WorkOrderTicketSubscriberFacade;
import com.baiyi.cratos.workorder.state.TicketState;
import com.baiyi.cratos.workorder.state.TicketStateChangeAction;
import com.baiyi.cratos.workorder.state.machine.factory.TicketInStateProcessorFactory;
import com.baiyi.cratos.wrapper.work.WorkOrderTicketDetailsWrapper;
import com.baiyi.cratos.wrapper.work.WorkOrderTicketWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/19 11:08
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
public class WorkOrderTicketFacadeImpl implements WorkOrderTicketFacade {

    private final WorkOrderService workOrderService;
    private final WorkOrderTicketService workOrderTicketService;
    private final WorkOrderTicketWrapper workOrderTicketWrapper;
    private final WorkOrderTicketNodeService workOrderTicketNodeService;
    private final WorkOrderTicketDetailsWrapper workOrderTicketDetailsWrapper;
    private final WorkOrderTicketNodeFacade workOrderTicketNodeFacade;
    private final UserService userService;
    private final WorkOrderTicketSubscriberFacade workOrderTicketSubscriberFacade;
    private final WorkOrderTicketEntryService workOrderTicketEntryService;

    @Override
    @SetSessionUserToParam(desc = "set Username")
    public DataTable<WorkOrderTicketVO.Ticket> queryMyTicketPage(WorkOrderTicketParam.MyTicketPageQuery pageQuery) {
        DataTable<WorkOrderTicket> table = workOrderTicketService.queryPageByParam(pageQuery);
        return workOrderTicketWrapper.wrapToTarget(table);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WorkOrderTicketVO.TicketDetails createTicket(WorkOrderTicketParam.CreateTicket createTicket) {
        final String ticketNo = PasswordGenerator.generateTicketNo();
        createTicket.setTicketNo(ticketNo);
        TicketEvent<WorkOrderTicketParam.CreateTicket> event = TicketEvent.of(createTicket);
        TicketInStateProcessorFactory.change(TicketState.CREATE, TicketStateChangeAction.CREATE, event);
        return getTicket(ticketNo);
    }

    @Override
    public WorkOrderTicketVO.TicketDetails getTicket(String ticketNo) {
        WorkOrderTicketVO.TicketDetails details = WorkOrderTicketVO.TicketDetails.builder()
                .ticketNo(ticketNo)
                .build();
        workOrderTicketDetailsWrapper.wrap(details);
        return details;
    }

    private WorkOrderTicketVO.TicketDetails getTicket(WorkOrderTicketParam.HasTicketNo hasTicketNo) {
        WorkOrderTicket ticket = workOrderTicketService.getByTicketNo(hasTicketNo.getTicketNo());
        return getTicket(ticket.getTicketNo());
    }

    @Override
    public WorkOrderTicketVO.TicketDetails submitTicket(WorkOrderTicketParam.SubmitTicket submitTicket) {
        TicketEvent<WorkOrderTicketParam.SubmitTicket> event = TicketEvent.of(submitTicket);
        TicketInStateProcessorFactory.change(TicketState.NEW, TicketStateChangeAction.SUBMIT, event);
        return getTicket(submitTicket);
    }

    @Override
    public WorkOrderTicketVO.TicketDetails approvalTicket(WorkOrderTicketParam.ApprovalTicket approvalTicket) {
        TicketEvent<WorkOrderTicketParam.ApprovalTicket> event = TicketEvent.of(approvalTicket);
        TicketInStateProcessorFactory.change(TicketState.IN_APPROVAL, TicketStateChangeAction.APPROVAL, event);
        return getTicket(approvalTicket);
    }

    @Override
    public void deleteTicketById(int ticketId) {
        WorkOrderTicket ticket = workOrderTicketService.getById(ticketId);
        if (Objects.nonNull(ticket)) {
            ticket.setValid(false);
            workOrderTicketService.updateByPrimaryKey(ticket);
        }
    }

}
