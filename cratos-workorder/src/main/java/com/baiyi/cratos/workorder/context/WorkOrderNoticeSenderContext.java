package com.baiyi.cratos.workorder.context;

import com.baiyi.cratos.eds.core.facade.EdsDingtalkMessageFacade;
import com.baiyi.cratos.service.NotificationTemplateService;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.util.LanguageUtils;
import com.baiyi.cratos.workorder.facade.TicketWorkflowFacade;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/3/20 17:58
 * &#064;Version 1.0
 */
@Getter
@Component
@RequiredArgsConstructor
public class WorkOrderNoticeSenderContext {

    private final WorkOrderTicketEntryService workOrderTicketEntryService;
    private final UserService userService;
    private final TicketWorkflowFacade ticketWorkflowFacade;
    private final EdsDingtalkMessageFacade edsDingtalkMessageFacade;
    private final LanguageUtils languageUtils;
    private final NotificationTemplateService notificationTemplateService;

}
