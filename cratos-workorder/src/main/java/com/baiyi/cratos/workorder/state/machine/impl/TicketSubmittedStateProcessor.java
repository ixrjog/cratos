package com.baiyi.cratos.workorder.state.machine.impl;

import com.baiyi.cratos.domain.generator.WorkOrder;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.domain.generator.WorkOrderTicketNode;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.domain.util.LanguageUtils;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.service.work.WorkOrderTicketNodeService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.workorder.annotation.TicketStates;
import com.baiyi.cratos.workorder.enums.TicketState;
import com.baiyi.cratos.workorder.event.TicketEvent;
import com.baiyi.cratos.workorder.facade.TicketWorkflowFacade;
import com.baiyi.cratos.workorder.facade.WorkOrderTicketNodeFacade;
import com.baiyi.cratos.workorder.facade.WorkOrderTicketSubscriberFacade;
import com.baiyi.cratos.workorder.state.TicketStateChangeAction;
import com.baiyi.cratos.workorder.state.machine.BaseTicketStateProcessor;
import com.baiyi.cratos.workorder.notice.WorkOrderApprovalNoticeHelper;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/21 11:34
 * &#064;Version 1.0
 */
@Component
@TicketStates(state = TicketState.SUBMITTED, target = TicketState.IN_APPROVAL)
public class TicketSubmittedStateProcessor extends BaseTicketStateProcessor<WorkOrderTicketParam.SimpleTicketNo> {

    private final WorkOrderApprovalNoticeHelper workOrderApprovalNoticeHelper;

    public TicketSubmittedStateProcessor(UserService userService, WorkOrderService workOrderService,
                                         WorkOrderTicketService workOrderTicketService,
                                         WorkOrderTicketNodeService workOrderTicketNodeService,
                                         WorkOrderTicketSubscriberFacade workOrderTicketSubscriberFacade,
                                         WorkOrderTicketNodeFacade workOrderTicketNodeFacade,
                                         WorkOrderTicketEntryService workOrderTicketEntryService,
                                         LanguageUtils languageUtils, TicketWorkflowFacade ticketWorkflowFacade,
                                         WorkOrderApprovalNoticeHelper approvalNotificationHelper) {
        super(userService, workOrderService, workOrderTicketService, workOrderTicketNodeService,
                workOrderTicketSubscriberFacade, workOrderTicketNodeFacade, workOrderTicketEntryService, languageUtils,
                ticketWorkflowFacade);
        this.workOrderApprovalNoticeHelper = approvalNotificationHelper;
    }

    @Override
    protected boolean isTransition(WorkOrderTicketParam.HasTicketNo hasTicketNo) {
        return true;
    }

    @Override
    protected boolean nextState(TicketStateChangeAction action,
                                TicketEvent<WorkOrderTicketParam.SimpleTicketNo> event) {
        return true;
    }

    @Override
    protected void processing(TicketStateChangeAction action, TicketEvent<WorkOrderTicketParam.SimpleTicketNo> event) {
        // 发布订阅关系
        WorkOrderTicket ticket = getTicketByNo(event.getBody()
                .getTicketNo());
        workOrderTicketSubscriberFacade.publish(ticket);
        // 发送通知
        if (ticket.getNodeId() == 0) {
            // 无审批节点
            return;
        }
        WorkOrderTicketNode ticketNode = workOrderTicketNodeService.getById(ticket.getNodeId());
        WorkOrder workOrder = workOrderService.getById(ticket.getWorkOrderId());
        workOrderApprovalNoticeHelper.sendMsg(workOrder, ticket, ticketNode);
    }

}

