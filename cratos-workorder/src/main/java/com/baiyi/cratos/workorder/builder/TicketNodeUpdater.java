package com.baiyi.cratos.workorder.builder;

import com.baiyi.cratos.domain.generator.WorkOrderTicketNode;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.service.work.WorkOrderTicketNodeService;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/18 10:45
 * &#064;Version 1.0
 */
public class TicketNodeUpdater {

    private WorkOrderTicketNode node;
    private WorkOrderTicketParam.ApprovalTicket approvalTicket;
    private String username;
    private WorkOrderTicketNodeService service;

    public static TicketNodeUpdater newUpdater() {
        return new TicketNodeUpdater();
    }

    public TicketNodeUpdater withNode(WorkOrderTicketNode node) {
        this.node = node;
        return this;
    }

    public TicketNodeUpdater withUsername(String username) {
        this.username = username;
        return this;
    }

    public TicketNodeUpdater withApprovalTicket(WorkOrderTicketParam.ApprovalTicket approvalTicket) {
        this.approvalTicket = approvalTicket;
        return this;
    }

    public TicketNodeUpdater withService(WorkOrderTicketNodeService service) {
        this.service = service;
        return this;
    }

    public void updateNode() {
        if (!StringUtils.hasText(node.getUsername())) {
            node.setUsername(username);
        }
        node.setApprovalAt(new Date());
        node.setApprovalCompleted(true);
        node.setApprovalStatus(approvalTicket.getApprovalType());
        node.setApproveRemark(approvalTicket.getApproveRemark());
        service.updateByPrimaryKey(node);
    }

}
