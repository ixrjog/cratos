package com.baiyi.cratos.workorder.facade;

import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.domain.generator.WorkOrderTicketNode;
import com.baiyi.cratos.domain.view.work.WorkOrderTicketVO;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/24 11:43
 * &#064;Version 1.0
 */
public interface TicketWorkflowFacade {

    List<User> querySelectableUsersByTags(List<String> tags);

    List<User> queryNodeApprovalUsers(WorkOrderTicket ticket, String nodeName);

    boolean isApprover(WorkOrderTicket ticket, String nodeName, String username);

    // boolean isApprover(WorkOrderTicketVO.Ticket ticket, String nodeName, String username);

    boolean isApprover(WorkOrderTicketVO.Ticket ticket, WorkOrderTicketNode ticketNode, String username);

    List<String> queryNodeApprovalUsernames(WorkOrderTicket ticket, String nodeName);

}
