package com.baiyi.cratos.workorder.state.machine.impl;

import com.baiyi.cratos.common.util.SessionUtils;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.service.work.WorkOrderTicketNodeService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.workorder.annotation.TicketStates;
import com.baiyi.cratos.workorder.event.TicketEvent;
import com.baiyi.cratos.workorder.exception.TicketStateProcessorException;
import com.baiyi.cratos.workorder.facade.WorkOrderTicketNodeFacade;
import com.baiyi.cratos.workorder.facade.WorkOrderTicketSubscriberFacade;
import com.baiyi.cratos.workorder.state.TicketState;
import com.baiyi.cratos.workorder.state.TicketStateChangeAction;
import com.baiyi.cratos.workorder.state.machine.BaseTicketStateProcessor;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/21 11:33
 * &#064;Version 1.0
 */
@Component
@TicketStates(state = TicketState.NEW, target = TicketState.SUBMITTED)
public class TicketNewStateProcessor extends BaseTicketStateProcessor<WorkOrderTicketParam.SubmitTicket> {

    public TicketNewStateProcessor(UserService userService, WorkOrderService workOrderService,
                                   WorkOrderTicketService workOrderTicketService,
                                   WorkOrderTicketNodeService workOrderTicketNodeService,
                                   WorkOrderTicketSubscriberFacade workOrderTicketSubscriberFacade,
                                   WorkOrderTicketNodeFacade workOrderTicketNodeFacade,
                                   WorkOrderTicketEntryService workOrderTicketEntryService) {
        super(userService, workOrderService, workOrderTicketService, workOrderTicketNodeService,
                workOrderTicketSubscriberFacade, workOrderTicketNodeFacade, workOrderTicketEntryService);
    }

    @Override
    protected void preChangeInspection(TicketStateChangeAction action,
                                       TicketEvent<WorkOrderTicketParam.SubmitTicket> event) {
        super.preChangeInspection(action, event);
        // 是否本人提交
        WorkOrderTicket ticket = getTicketByNo(event.getBody());
        if (!ticket.getUsername()
                .equals(SessionUtils.getUsername())) {
            TicketStateProcessorException.runtime("Non personal submission of work order.");
        }
        // 工单条目未配置
        if (workOrderTicketEntryService.countByTicketId(ticket.getId()) == 0) {
            TicketStateProcessorException.runtime("Work order entry not configured.");
        }
    }

    @Override
    protected boolean isTransition(WorkOrderTicketParam.HasTicketNo hasTicketNo) {
        return true;
    }

    @Override
    protected void processing(TicketStateChangeAction action, TicketEvent<WorkOrderTicketParam.SubmitTicket> event) {
        WorkOrderTicket ticket = getTicketByNo(event.getBody());
        ticket.setSubmittedAt(new Date());
        ticket.setApplyRemark(event.getBody()
                .getApplyRemark());
        workOrderTicketService.updateByPrimaryKey(ticket);
    }

}
