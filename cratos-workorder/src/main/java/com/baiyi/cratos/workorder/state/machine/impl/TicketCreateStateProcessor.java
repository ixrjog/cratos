package com.baiyi.cratos.workorder.state.machine.impl;

import com.baiyi.cratos.common.enums.AccessLevel;
import com.baiyi.cratos.common.util.SessionUtils;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.generator.WorkOrder;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.domain.generator.WorkOrderTicketNode;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.facade.RbacRoleFacade;
import com.baiyi.cratos.util.LanguageUtils;
import com.baiyi.cratos.workorder.annotation.StateForward;
import com.baiyi.cratos.workorder.annotation.TicketStates;
import com.baiyi.cratos.workorder.annotation.TransitionGuard;
import com.baiyi.cratos.workorder.builder.TicketBuilder;
import com.baiyi.cratos.workorder.context.TicketStateProcessorContext;
import com.baiyi.cratos.workorder.enums.TicketState;
import com.baiyi.cratos.workorder.enums.TicketStateChangeAction;
import com.baiyi.cratos.workorder.event.TicketEvent;
import com.baiyi.cratos.workorder.exception.WorkOrderTicketException;
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
@TransitionGuard
@StateForward(enabled = false)
public class TicketCreateStateProcessor extends BaseTicketStateProcessor<WorkOrderTicketParam.CreateTicket> {

    private final RbacRoleFacade rbacRoleFacade;
    private final LanguageUtils languageUtils;

    public TicketCreateStateProcessor(TicketStateProcessorContext context, RbacRoleFacade rbacRoleFacade,
                                      LanguageUtils languageUtils) {
        super(context);
        this.rbacRoleFacade = rbacRoleFacade;
        this.languageUtils = languageUtils;
    }


    @Override
    protected void preChangeInspection(TicketStateChangeAction action,
                                       TicketEvent<WorkOrderTicketParam.CreateTicket> event) {
        // 运维不限制工单数量
        if (rbacRoleFacade.verifyRoleAccessLevelByUsername(AccessLevel.OPS, SessionUtils.getUsername())) {
            return;
        }
        // 新建工单超过3个不允许继续创建
        WorkOrder workOrder = context.getWorkOrderService()
                .getByWorkOrderKey(event.getBody()
                                           .getWorkOrderKey());
        if (Objects.isNull(workOrder)) {
            WorkOrderTicketException.runtime(languageUtils.getMessage("workorder.not-exist"));
        }
        if (context.getWorkOrderTicketService()
                .countUserWorkOrderTicketByState(
                        SessionUtils.getUsername(), workOrder.getId(),
                        TicketState.NEW.name()
                ) >= 3) {
            String i18nMessage = languageUtils.getFormattedMessage(
                    "workorder.create.ticket.limit.exception.message", event.getBody()
                            .getWorkOrderKey()
            );
            WorkOrderTicketException.runtime(i18nMessage);
        }
    }

    @Override
    protected boolean isTransition(WorkOrderTicketParam.HasTicketNo hasTicketNo) {
        return true;
    }

    private void createWorkflowNodes(User user, WorkOrder workOrder, WorkOrderTicket newTicket) {
        context.getWorkOrderTicketNodeFacade()
                .createWorkflowNodes(workOrder, newTicket);
        WorkOrderTicketNode rootNode = context.getWorkOrderTicketNodeService()
                .getRootNode(newTicket.getId());
        if (Objects.nonNull(rootNode)) {
            newTicket.setNodeId(rootNode.getId());
            context.getWorkOrderTicketService()
                    .updateByPrimaryKey(newTicket);
        }
        context.getWorkOrderTicketSubscriberFacade()
                .publish(newTicket, user);
    }

    @Override
    protected boolean nextState(TicketStateChangeAction action, TicketEvent<WorkOrderTicketParam.CreateTicket> event) {
        return false;
    }

    /**
     * 创建工单
     *
     * @param action
     * @param event
     */
    @Override
    protected void processing(TicketStateChangeAction action, TicketEvent<WorkOrderTicketParam.CreateTicket> event) {
        final String username = SessionUtils.getUsername();
        User user = context.getUserService()
                .getByUsername(username);
        WorkOrder workOrder = context.getWorkOrderService()
                .getByWorkOrderKey(event.getBody()
                                           .getWorkOrderKey());
        if (Objects.isNull(workOrder)) {
            WorkOrderTicketException.runtime(languageUtils.getMessage("workorder.not-exist"));
        }
        WorkOrderTicket createTicket = TicketBuilder.newBuilder()
                .withWorkOrder(workOrder)
                .withUser(user)
                .withTicketNo(event.getBody()
                                      .getTicketNo())
                .newTicket();
        context.getWorkOrderTicketService()
                .add(createTicket);
        createWorkflowNodes(user, workOrder, createTicket);
    }

}