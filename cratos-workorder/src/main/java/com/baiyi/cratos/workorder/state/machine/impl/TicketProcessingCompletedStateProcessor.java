package com.baiyi.cratos.workorder.state.machine.impl;

import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.domain.util.LanguageUtils;
import com.baiyi.cratos.eds.core.facade.EdsDingtalkMessageFacade;
import com.baiyi.cratos.service.NotificationTemplateService;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.service.work.WorkOrderTicketNodeService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.workorder.annotation.TicketStates;
import com.baiyi.cratos.workorder.event.TicketEvent;
import com.baiyi.cratos.workorder.facade.TicketWorkflowFacade;
import com.baiyi.cratos.workorder.facade.WorkOrderTicketNodeFacade;
import com.baiyi.cratos.workorder.facade.WorkOrderTicketSubscriberFacade;
import com.baiyi.cratos.workorder.enums.TicketState;
import com.baiyi.cratos.workorder.state.TicketStateChangeAction;
import com.baiyi.cratos.workorder.state.machine.BaseTicketStateProcessor;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/21 13:47
 * &#064;Version 1.0
 */
@Component
@TicketStates(state = TicketState.PROCESSING_COMPLETED, target = TicketState.COMPLETED)
public class TicketProcessingCompletedStateProcessor extends BaseTicketStateProcessor<WorkOrderTicketParam.SimpleTicketNo> {

    public TicketProcessingCompletedStateProcessor(UserService userService, WorkOrderService workOrderService,
                                                   WorkOrderTicketService workOrderTicketService,
                                                   WorkOrderTicketNodeService workOrderTicketNodeService,
                                                   WorkOrderTicketSubscriberFacade workOrderTicketSubscriberFacade,
                                                   WorkOrderTicketNodeFacade workOrderTicketNodeFacade,
                                                   WorkOrderTicketEntryService workOrderTicketEntryService,
                                                   NotificationTemplateService notificationTemplateService,
                                                   EdsDingtalkMessageFacade edsDingtalkMessageFacade,
                                                   LanguageUtils languageUtils,
                                                   TicketWorkflowFacade ticketWorkflowFacade) {
        super(userService, workOrderService, workOrderTicketService, workOrderTicketNodeService,
                workOrderTicketSubscriberFacade, workOrderTicketNodeFacade, workOrderTicketEntryService,
                notificationTemplateService, edsDingtalkMessageFacade, languageUtils, ticketWorkflowFacade);
    }

    @Override
    protected boolean isTransition(WorkOrderTicketParam.HasTicketNo hasTicketNo) {
        return false;
    }

    protected boolean nextState(TicketStateChangeAction action,
                                TicketEvent<WorkOrderTicketParam.SimpleTicketNo> event) {
        return false;
    }

}
