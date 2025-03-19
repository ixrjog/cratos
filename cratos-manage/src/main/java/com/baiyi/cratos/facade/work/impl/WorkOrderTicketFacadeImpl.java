package com.baiyi.cratos.facade.work.impl;

import com.baiyi.cratos.common.exception.WorkOrderTicketException;
import com.baiyi.cratos.common.util.SessionUtils;
import com.baiyi.cratos.domain.generator.*;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.domain.view.work.WorkOrderTicketVO;
import com.baiyi.cratos.facade.work.WorkOrderTicketFacade;
import com.baiyi.cratos.facade.work.WorkOrderTicketNodeFacade;
import com.baiyi.cratos.facade.work.WorkOrderTicketSubscriberFacade;
import com.baiyi.cratos.facade.work.builder.TicketBuilder;
import com.baiyi.cratos.facade.work.entry.TicketEntryProvider;
import com.baiyi.cratos.facade.work.entry.TicketEntryProviderFactory;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.service.work.WorkOrderTicketNodeService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.workorder.enums.WorkOrderKeys;
import com.baiyi.cratos.wrapper.work.WorkOrderTicketDetailsWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/19 11:08
 * &#064;Version 1.0
 */
@SuppressWarnings("unchecked")
@Component
@RequiredArgsConstructor
public class WorkOrderTicketFacadeImpl implements WorkOrderTicketFacade {

    private final WorkOrderService workOrderService;
    private final WorkOrderTicketService workOrderTicketService;
    private final WorkOrderTicketNodeService workOrderTicketNodeService;
    private final WorkOrderTicketDetailsWrapper workOrderTicketDetailsWrapper;
    private final WorkOrderTicketNodeFacade workOrderTicketNodeFacade;
    private final UserService userService;
    private final WorkOrderTicketSubscriberFacade workOrderTicketSubscriberFacade;
    private final WorkOrderTicketEntryService workOrderTicketEntryService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WorkOrderTicketVO.TicketDetails createTicket(WorkOrderTicketParam.CreateTicket createTicket) {
        final String username = SessionUtils.getUsername();
        User user = userService.getByUsername(username);
        WorkOrder workOrder = workOrderService.getByWorkOrderKey(createTicket.getWorkOrderKey());
        if (Objects.isNull(workOrder)) {
            WorkOrderTicketException.runtime("The work order does not exist.");
        }
        WorkOrderTicket newTicket = TicketBuilder.newBuilder()
                .withWorkOrder(workOrder)
                .withUser(user)
                .newTicket();
        workOrderTicketService.add(newTicket);
        createWorkflowNodes(user, workOrder, newTicket);
        return getTicket(newTicket.getTicketId());
    }

    @Override
    public WorkOrderTicketVO.TicketDetails getTicket(String ticketId) {
        WorkOrderTicketVO.TicketDetails details = WorkOrderTicketVO.TicketDetails.builder()
                .ticketId(ticketId)
                .build();
        workOrderTicketDetailsWrapper.wrap(details);
        return details;
    }

    @Override
    public void deleteTicketById(int ticketId) {
        WorkOrderTicket ticket = workOrderTicketService.getById(ticketId);
        if (Objects.nonNull(ticket)) {
            ticket.setValid(false);
            workOrderTicketService.updateByPrimaryKey(ticket);
        }
    }

    private void createWorkflowNodes(User user, WorkOrder workOrder, WorkOrderTicket newTicket) {
        workOrderTicketNodeFacade.createWorkflowNodes(workOrder, newTicket);
        WorkOrderTicketVO.TicketDetails details = WorkOrderTicketVO.TicketDetails.builder()
                .ticketId(newTicket.getTicketId())
                .build();
        WorkOrderTicketNode rootNode = workOrderTicketNodeService.getRootNode(newTicket.getId());
        newTicket.setNodeId(rootNode.getId());
        workOrderTicketService.updateByPrimaryKey(newTicket);
        workOrderTicketSubscriberFacade.publish(newTicket, user);
    }


}
