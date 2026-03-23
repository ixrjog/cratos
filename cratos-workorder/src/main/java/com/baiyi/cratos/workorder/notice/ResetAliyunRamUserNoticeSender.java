package com.baiyi.cratos.workorder.notice;

import com.baiyi.cratos.common.builder.SimpleMapBuilder;
import com.baiyi.cratos.common.enums.NotificationTemplateKeys;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.generator.WorkOrder;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.workorder.context.WorkOrderNoticeSenderContext;
import com.baiyi.cratos.workorder.notice.base.BaseWorkOrderNoticeSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/5/26 10:39
 * &#064;Version 1.0
 */
@Slf4j
@Component
public class ResetAliyunRamUserNoticeSender extends BaseWorkOrderNoticeSender {

    public ResetAliyunRamUserNoticeSender(WorkOrderNoticeSenderContext context) {
        super(context);
    }

    public void sendMsg(WorkOrder workOrder, WorkOrderTicket ticket, String ramLoginUsername, String password,
                        String loginLink, User applicantUser) {
        Map<String, Object> dict = SimpleMapBuilder.newBuilder()
                .put("ticketNo", ticket.getTicketNo())
                .put("workOrderName", workOrder.getName())
                .put("applicant", ticket.getUsername())
                .put("aliyunRAMUsername", ramLoginUsername)
                .put("password", password)
                .put("loginLink", loginLink)
                .build();
        sendMsgToUser(applicantUser, dict);
    }

    private void sendMsgToUser(User sendToUser, Map<String, Object> dict) {
        sendMsgToUser(sendToUser, NotificationTemplateKeys.RESET_ALIYUN_RAM_USER_NOTICE, dict);
    }

}
