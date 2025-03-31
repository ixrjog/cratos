package com.baiyi.cratos.workorder.util;

import com.baiyi.cratos.common.builder.SimpleMapBuilder;
import com.baiyi.cratos.common.util.beetl.BeetlUtil;
import com.baiyi.cratos.domain.generator.*;
import com.baiyi.cratos.domain.util.LanguageUtils;
import com.baiyi.cratos.eds.core.facade.EdsDingtalkMessageFacade;
import com.baiyi.cratos.service.NotificationTemplateService;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.workorder.facade.TicketWorkflowFacade;
import com.baiyi.cratos.workorder.model.TicketEntryModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.baiyi.cratos.common.enums.NotificationTemplateKeys.WORK_ORDER_COMPLETION_NOTICE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/31 15:47
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicantNotificationHelper {

    private final WorkOrderTicketEntryService workOrderTicketEntryService;
    private final UserService userService;
    private final TicketWorkflowFacade ticketWorkflowFacade;
    private final EdsDingtalkMessageFacade edsDingtalkMessageFacade;
    private final LanguageUtils languageUtils;
    private final NotificationTemplateService notificationTemplateService;

    public void sendMsg(WorkOrder workOrder, WorkOrderTicket ticket) {
        User applicantUser = userService.getByUsername(ticket.getUsername());
        List<TicketEntryModel.EntryDesc> ticketEntities = workOrderTicketEntryService.queryTicketEntries(ticket.getId())
                .stream()
                .map(e ->

                        TicketEntryModel.EntryDesc.builder()
                                .name(e.getName())
                                .desc(e.getSuccess() ? "Success" : "Failed")
                                .build()

                )
                .toList();
        Map<String, Object> dict = SimpleMapBuilder.newBuilder()
                .put("ticketNo", ticket.getTicketNo())
                .put("workOrderName", workOrder.getName())
                .put("ticketEntities ", ticketEntities)
                .build();
        sendMsgToApplicant(applicantUser, dict);
    }

    protected void sendMsgToApplicant(User applicantUser, Map<String, Object> dict) {
        try {
            NotificationTemplate notificationTemplate = getNotificationTemplate(WORK_ORDER_COMPLETION_NOTICE.name(),
                    applicantUser);
            String msg = BeetlUtil.renderTemplate(notificationTemplate.getContent(), dict);
            edsDingtalkMessageFacade.sendToDingtalkUser(applicantUser, notificationTemplate, msg);
        } catch (IOException ioException) {
            log.error("WorkOrder ticket send msg to user err: {}", ioException.getMessage());
        }
    }

    private NotificationTemplate getNotificationTemplate(String notificationTemplateKey, User user) {
        NotificationTemplate query = NotificationTemplate.builder()
                .notificationTemplateKey(notificationTemplateKey)
                .lang(languageUtils.getUserLanguage(user))
                .build();
        return notificationTemplateService.getByUniqueKey(query);
    }

}
