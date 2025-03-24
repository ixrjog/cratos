package com.baiyi.cratos.workorder.facade;

import com.baiyi.cratos.domain.generator.WorkOrder;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/18 11:17
 * &#064;Version 1.0
 */
public interface WorkOrderTicketNodeFacade {

    void createWorkflowNodes(WorkOrder workOrder, WorkOrderTicket newTicket);

    void verifyWorkflowNodes(WorkOrder workOrder, WorkOrderTicket workOrderTicket);

    void specifyNodeApprovalUser(int ticketId, String nodeName, String username);

}
