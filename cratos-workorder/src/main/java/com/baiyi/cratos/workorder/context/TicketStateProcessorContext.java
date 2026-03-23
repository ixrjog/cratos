package com.baiyi.cratos.workorder.context;

import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.service.work.WorkOrderTicketNodeService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.workorder.facade.TicketWorkflowFacade;
import com.baiyi.cratos.workorder.facade.WorkOrderTicketNodeFacade;
import com.baiyi.cratos.workorder.facade.WorkOrderTicketSubscriberFacade;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/3/20 18:04
 * &#064;Version 1.0
 */
@Getter
@Component
@RequiredArgsConstructor
public class TicketStateProcessorContext {

    private final UserService userService;
    private final WorkOrderService workOrderService;
    private final WorkOrderTicketService workOrderTicketService;
    private final WorkOrderTicketNodeService workOrderTicketNodeService;
    private final WorkOrderTicketSubscriberFacade workOrderTicketSubscriberFacade;
    private final WorkOrderTicketNodeFacade workOrderTicketNodeFacade;
    private final WorkOrderTicketEntryService workOrderTicketEntryService;
    private final TicketWorkflowFacade ticketWorkflowFacade;

}
