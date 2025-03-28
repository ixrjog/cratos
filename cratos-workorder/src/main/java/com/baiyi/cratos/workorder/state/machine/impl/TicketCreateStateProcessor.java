package com.baiyi.cratos.workorder.state.machine.impl;

import com.baiyi.cratos.workorder.exception.WorkOrderTicketException;
import com.baiyi.cratos.common.util.SessionUtils;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.generator.WorkOrder;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.domain.generator.WorkOrderTicketNode;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.domain.view.work.WorkOrderTicketVO;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.service.work.WorkOrderTicketNodeService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.workorder.annotation.TicketStates;
import com.baiyi.cratos.workorder.builder.TicketBuilder;
import com.baiyi.cratos.workorder.event.TicketEvent;
import com.baiyi.cratos.workorder.facade.WorkOrderTicketNodeFacade;
import com.baiyi.cratos.workorder.facade.WorkOrderTicketSubscriberFacade;
import com.baiyi.cratos.workorder.enums.TicketState;
import com.baiyi.cratos.workorder.state.TicketStateChangeAction;
import com.baiyi.cratos.workorder.state.machine.BaseTicketStateProcessor;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/24 10:22
 * &#064;Version 1.0
 */
@Component
@TicketStates(state = TicketState.CREATE, target = TicketState.NEW)
public class TicketCreateStateProcessor extends BaseTicketStateProcessor<WorkOrderTicketParam.CreateTicket> {

    public TicketCreateStateProcessor(UserService userService, WorkOrderService workOrderService,
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
                                       TicketEvent<WorkOrderTicketParam.CreateTicket> event) {
        // 新建工单超过3个不允许继续创建
        //  WorkOrderTicketException.runtime("The work order does not exist.");
    }

    @Override
    protected boolean isTransition(WorkOrderTicketParam.HasTicketNo hasTicketNo) {
        return true;
    }

    private void createWorkflowNodes(User user, WorkOrder workOrder, WorkOrderTicket newTicket) {
        workOrderTicketNodeFacade.createWorkflowNodes(workOrder, newTicket);
        WorkOrderTicketVO.TicketDetails details = WorkOrderTicketVO.TicketDetails.builder()
                .ticketNo(newTicket.getTicketNo())
                .build();
        WorkOrderTicketNode rootNode = workOrderTicketNodeService.getRootNode(newTicket.getId());
        newTicket.setNodeId(rootNode.getId());
        workOrderTicketService.updateByPrimaryKey(newTicket);
        workOrderTicketSubscriberFacade.publish(newTicket, user);
    }

    @Override
    protected boolean nextState(TicketStateChangeAction action) {
        return false;
    }

    /**
     * 创建工单
     * @param action
     * @param event
     */
    @Override
    protected void processing(TicketStateChangeAction action, TicketEvent<WorkOrderTicketParam.CreateTicket> event) {
        final String username = SessionUtils.getUsername();
        User user = userService.getByUsername(username);
        WorkOrder workOrder = workOrderService.getByWorkOrderKey(event.getBody()
                .getWorkOrderKey());
        if (Objects.isNull(workOrder)) {
            WorkOrderTicketException.runtime("The work order does not exist.");
        }
        WorkOrderTicket createTicket = TicketBuilder.newBuilder()
                .withWorkOrder(workOrder)
                .withUser(user)
                .withTicketNo(event.getBody()
                        .getTicketNo())
                .newTicket();
        workOrderTicketService.add(createTicket);
        createWorkflowNodes(user, workOrder, createTicket);
    }

}