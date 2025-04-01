package com.baiyi.cratos.workorder.notice.base;

import com.baiyi.cratos.domain.generator.NotificationTemplate;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.util.LanguageUtils;
import com.baiyi.cratos.eds.core.facade.EdsDingtalkMessageFacade;
import com.baiyi.cratos.service.NotificationTemplateService;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.workorder.facade.TicketWorkflowFacade;
import lombok.RequiredArgsConstructor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/1 14:05
 * &#064;Version 1.0
 */
@RequiredArgsConstructor
public abstract class BaseWorkOrderNoticeHelper {

    protected final WorkOrderTicketEntryService workOrderTicketEntryService;
    protected final UserService userService;
    protected final TicketWorkflowFacade ticketWorkflowFacade;
    protected final EdsDingtalkMessageFacade edsDingtalkMessageFacade;
    private final LanguageUtils languageUtils;
    private final NotificationTemplateService notificationTemplateService;

    protected NotificationTemplate getNotificationTemplate(String notificationTemplateKey, User user) {
        NotificationTemplate query = NotificationTemplate.builder()
                .notificationTemplateKey(notificationTemplateKey)
                .lang(languageUtils.getUserLanguage(user))
                .build();
        return notificationTemplateService.getByUniqueKey(query);
    }

}
