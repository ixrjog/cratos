package com.baiyi.cratos.facade.work.impl;

import com.baiyi.cratos.annotation.SetSessionUserToParam;
import com.baiyi.cratos.common.enums.AccessLevel;
import com.baiyi.cratos.common.util.PasswordGenerator;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.WorkOrder;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.domain.view.work.WorkOrderTicketVO;
import com.baiyi.cratos.facade.RbacRoleFacade;
import com.baiyi.cratos.facade.work.WorkOrderTicketFacade;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.workorder.enums.TicketState;
import com.baiyi.cratos.workorder.enums.TicketStateChangeAction;
import com.baiyi.cratos.workorder.enums.WorkOrderStatus;
import com.baiyi.cratos.workorder.event.TicketEvent;
import com.baiyi.cratos.workorder.exception.WorkOrderException;
import com.baiyi.cratos.workorder.state.machine.factory.TicketInStateProcessorFactory;
import com.baiyi.cratos.wrapper.work.WorkOrderTicketDetailsWrapper;
import com.baiyi.cratos.wrapper.work.WorkOrderTicketWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
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
    private final WorkOrderTicketDetailsWrapper workOrderTicketDetailsWrapper;
    private final RbacRoleFacade rbacRoleFacade;

    @Override
    @SetSessionUserToParam(desc = "set Username")
    public DataTable<WorkOrderTicketVO.Ticket> queryMyTicketPage(WorkOrderTicketParam.MyTicketPageQuery pageQuery) {
        DataTable<WorkOrderTicket> table = workOrderTicketService.queryPageByParam(pageQuery);
        return workOrderTicketWrapper.wrapToTarget(table);
    }

    @Override
    public DataTable<WorkOrderTicketVO.Ticket> queryTicketPage(WorkOrderTicketParam.TicketPageQuery pageQuery) {
        DataTable<WorkOrderTicket> table = workOrderTicketService.queryPageByParam(pageQuery);
        return workOrderTicketWrapper.wrapToTarget(table);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WorkOrderTicketVO.TicketDetails createTicket(WorkOrderTicketParam.CreateTicket createTicket) {
        WorkOrder workOrder = workOrderService.getByWorkOrderKey(createTicket.getWorkOrderKey());
        // 开发中
        if (WorkOrderStatus.DEVELOPING.equals(WorkOrderStatus.valueOf(workOrder.getStatus()))) {
            if (!rbacRoleFacade.verifyRoleAccessLevelByUsername(AccessLevel.OPS)) {
                WorkOrderException.runtime(
                        "Work order development in progress. Please wait for the work order to be completed.");
            }
        }
        final String ticketNo = PasswordGenerator.generateTicketNo();
        createTicket.setTicketNo(ticketNo);
        TicketEvent<WorkOrderTicketParam.CreateTicket> event = TicketEvent.of(createTicket);
        TicketInStateProcessorFactory.change(TicketState.CREATE, TicketStateChangeAction.CREATE, event);
        return makeTicketDetails(ticketNo);
    }

    @Override
    public WorkOrderTicketVO.TicketDetails makeTicketDetails(String ticketNo) {
        WorkOrderTicketVO.TicketDetails details = WorkOrderTicketVO.TicketDetails.builder()
                .ticketNo(ticketNo)
                .build();
        workOrderTicketDetailsWrapper.wrap(details);
        return details;
    }

    private WorkOrderTicketVO.TicketDetails makeTicketDetails(WorkOrderTicketParam.HasTicketNo hasTicketNo) {
        return makeTicketDetails(hasTicketNo.getTicketNo());
    }

    @Override
    public WorkOrderTicketVO.TicketDetails submitTicket(WorkOrderTicketParam.SubmitTicket submitTicket) {
        TicketEvent<WorkOrderTicketParam.SubmitTicket> event = TicketEvent.of(submitTicket);
        TicketInStateProcessorFactory.change(TicketState.NEW, TicketStateChangeAction.SUBMIT, event);
        return makeTicketDetails(submitTicket);
    }

    @Override
    public WorkOrderTicketVO.TicketDetails doNextStateOfTicket(WorkOrderTicketParam.SimpleTicketNo simpleTicketNo) {
        TicketEvent<WorkOrderTicketParam.SimpleTicketNo> event = TicketEvent.of(simpleTicketNo);
        WorkOrderTicket ticket = workOrderTicketService.getByTicketNo(simpleTicketNo.getTicketNo());
        TicketInStateProcessorFactory.change(TicketState.valueOf(ticket.getTicketState()),
                TicketStateChangeAction.DO_NEXT, event);
        return makeTicketDetails(simpleTicketNo);
    }

    @Override
    @Async
    public void approvalTicket(WorkOrderTicketParam.ApprovalTicket approvalTicket) {
        TicketEvent<WorkOrderTicketParam.ApprovalTicket> event = TicketEvent.of(approvalTicket);
        TicketInStateProcessorFactory.change(TicketState.IN_APPROVAL, TicketStateChangeAction.APPROVAL, event);
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
