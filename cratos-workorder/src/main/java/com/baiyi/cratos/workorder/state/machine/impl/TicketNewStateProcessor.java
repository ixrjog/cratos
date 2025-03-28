package com.baiyi.cratos.workorder.state.machine.impl;

import com.baiyi.cratos.common.util.SessionUtils;
import com.baiyi.cratos.domain.generator.WorkOrder;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.domain.generator.WorkOrderTicketNode;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.service.work.WorkOrderTicketNodeService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.workorder.annotation.TicketStates;
import com.baiyi.cratos.workorder.enums.ApprovalTypes;
import com.baiyi.cratos.workorder.enums.TicketState;
import com.baiyi.cratos.workorder.event.TicketEvent;
import com.baiyi.cratos.workorder.exception.TicketStateProcessorException;
import com.baiyi.cratos.workorder.facade.TicketWorkflowFacade;
import com.baiyi.cratos.workorder.facade.WorkOrderTicketNodeFacade;
import com.baiyi.cratos.workorder.facade.WorkOrderTicketSubscriberFacade;
import com.baiyi.cratos.workorder.state.TicketStateChangeAction;
import com.baiyi.cratos.workorder.state.machine.BaseTicketStateProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/21 11:33
 * &#064;Version 1.0
 */
@Component
@TicketStates(state = TicketState.NEW, target = TicketState.SUBMITTED)
public class TicketNewStateProcessor extends BaseTicketStateProcessor<WorkOrderTicketParam.SubmitTicket> {

    private final TicketWorkflowFacade ticketWorkflowFacade;

    public TicketNewStateProcessor(UserService userService, WorkOrderService workOrderService,
                                   WorkOrderTicketService workOrderTicketService,
                                   WorkOrderTicketNodeService workOrderTicketNodeService,
                                   WorkOrderTicketSubscriberFacade workOrderTicketSubscriberFacade,
                                   WorkOrderTicketNodeFacade workOrderTicketNodeFacade,
                                   WorkOrderTicketEntryService workOrderTicketEntryService,
                                   TicketWorkflowFacade ticketWorkflowFacade) {
        super(userService, workOrderService, workOrderTicketService, workOrderTicketNodeService,
                workOrderTicketSubscriberFacade, workOrderTicketNodeFacade, workOrderTicketEntryService);
        this.ticketWorkflowFacade = ticketWorkflowFacade;
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
        // 校验节点是否都指定了审批人
        List<WorkOrderTicketNode> nodes = workOrderTicketNodeService.queryByTicketId(ticket.getId());
        if (!CollectionUtils.isEmpty(nodes)) {
            Map<String, String> nodeApprover = Optional.ofNullable(event.getBody())
                    .map(WorkOrderTicketParam.SubmitTicket::toApprovers)
                    .orElse(Map.of());
            nodes.stream()
                    .filter(node -> ApprovalTypes.USER_SPECIFIED.equals(ApprovalTypes.valueOf(node.getApprovalType())))
                    .filter(node -> !StringUtils.hasText(node.getUsername()) && !nodeApprover.containsKey(
                            node.getNodeName()))
                    .findAny()
                    .ifPresent(
                            node -> TicketStateProcessorException.runtime("Approval node {} does not specify approver.",
                                    node.getNodeName()));
        }
    }

    @Override
    protected boolean nextState(TicketStateChangeAction action) {
        return TicketStateChangeAction.SUBMIT.equals(action);
    }

    @Override
    protected boolean isTransition(WorkOrderTicketParam.HasTicketNo hasTicketNo) {
        return true;
    }

    @Override
    protected void processing(TicketStateChangeAction action, TicketEvent<WorkOrderTicketParam.SubmitTicket> event) {
        // 更新审批节点信息
        WorkOrderTicket ticket = getTicketByNo(event.getBody());
        Map<String, String> nodeApprover = Optional.ofNullable(event.getBody())
                .map(WorkOrderTicketParam.SubmitTicket::toApprovers)
                .orElse(Map.of());
        WorkOrder workOrder = workOrderService.getById(ticket.getWorkOrderId());
        workOrderTicketNodeService.queryByTicketId(ticket.getId())
                .stream()
                .filter(node -> ApprovalTypes.USER_SPECIFIED.equals(
                        ApprovalTypes.valueOf(node.getApprovalType())) && nodeApprover.containsKey(node.getNodeName()))
                .forEach(node -> {
                    if (ticketWorkflowFacade.isApprover(workOrder, node.getNodeName(),
                            nodeApprover.get(node.getNodeName()))) {
                        node.setUsername(nodeApprover.get(node.getNodeName()));
                        workOrderTicketNodeService.updateByPrimaryKey(node);
                    } else {
                        TicketStateProcessorException.runtime("Invalid approver.");
                    }
                });
        // 更新工单状态
        ticket.setSubmittedAt(new Date());
        ticket.setApplyRemark(event.getBody()
                .getApplyRemark());
        workOrderTicketService.updateByPrimaryKey(ticket);
    }

}
