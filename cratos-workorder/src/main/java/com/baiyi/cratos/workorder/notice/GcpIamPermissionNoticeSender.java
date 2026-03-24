package com.baiyi.cratos.workorder.notice;

import com.baiyi.cratos.common.builder.SimpleMapBuilder;
import com.baiyi.cratos.common.enums.NotificationTemplateKeys;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.generator.WorkOrder;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.domain.model.GcpModel;
import com.baiyi.cratos.workorder.context.WorkOrderNoticeSenderContext;
import com.baiyi.cratos.workorder.notice.base.BaseWorkOrderNoticeSender;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/3/24 11:35
 * &#064;Version 1.0
 */
@Component
public class GcpIamPermissionNoticeSender extends BaseWorkOrderNoticeSender {

    public GcpIamPermissionNoticeSender(WorkOrderNoticeSenderContext context) {
        super(context);
    }

    public void sendMsg(WorkOrder workOrder, WorkOrderTicket ticket, GcpModel.IamMember iamMember, User applicantUser) {
        Map<String, Object> dict = SimpleMapBuilder.newBuilder()
                .put("ticketNo", ticket.getTicketNo())
                .put("workOrderName", workOrder.getName())
                .put("applicant", ticket.getUsername())
                .put("member", iamMember.getMember())
                .put("projectName", iamMember.getProjectName())
                .put("projectId", iamMember.getProjectId())
                .put("loginLink", iamMember.getLoginLink())
                .build();
        sendMsgToUser(applicantUser, dict);
    }

    private void sendMsgToUser(User sendToUser, Map<String, Object> dict) {
        sendMsgToUser(sendToUser, NotificationTemplateKeys.GCP_IAM_PERMISSION_NOTICE, dict);
    }

}
