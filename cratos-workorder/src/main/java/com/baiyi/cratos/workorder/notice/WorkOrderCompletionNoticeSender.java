package com.baiyi.cratos.workorder.notice;

import com.baiyi.cratos.common.builder.SimpleMapBuilder;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.generator.WorkOrder;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.workorder.context.WorkOrderNoticeSenderContext;
import com.baiyi.cratos.workorder.model.TicketEntryModel;
import com.baiyi.cratos.workorder.notice.base.BaseWorkOrderNoticeSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
public class WorkOrderCompletionNoticeSender extends BaseWorkOrderNoticeSender {

    public WorkOrderCompletionNoticeSender(WorkOrderNoticeSenderContext context) {
        super(context);
    }

    public void sendMsg(WorkOrder workOrder, WorkOrderTicket ticket) {
        User applicantUser = context.getUserService().getByUsername(ticket.getUsername());
        List<TicketEntryModel.EntryDesc> ticketEntities = context.getWorkOrderTicketEntryService().queryTicketEntries(ticket.getId())
                .stream()
                .map(e -> TicketEntryModel.EntryDesc.builder()
                        .name(e.getName())
                        .desc(e.getSuccess() ? "Success" : "Failed")
                        .build())
                .toList();
        Map<String, Object> dict = SimpleMapBuilder.newBuilder()
                .put("ticketNo", ticket.getTicketNo())
                .put("workOrderName", workOrder.getName())
                .put("ticketEntities", ticketEntities)
                .build();
        sendMsgToApplicant(applicantUser, dict);
    }

    protected void sendMsgToApplicant(User applicantUser, Map<String, Object> dict) {
        sendMsgToUser(applicantUser, WORK_ORDER_COMPLETION_NOTICE, dict);
    }

}
