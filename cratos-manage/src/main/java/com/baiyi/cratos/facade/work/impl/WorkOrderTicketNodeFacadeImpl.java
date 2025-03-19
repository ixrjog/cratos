package com.baiyi.cratos.facade.work.impl;

import com.baiyi.cratos.common.exception.WorkOrderTicketException;
import com.baiyi.cratos.domain.generator.WorkOrder;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.domain.generator.WorkOrderTicketNode;
import com.baiyi.cratos.facade.work.WorkOrderTicketNodeFacade;
import com.baiyi.cratos.service.work.WorkOrderTicketNodeService;
import com.baiyi.cratos.workorder.enums.ApprovalTypes;
import com.baiyi.cratos.domain.model.WorkflowModel;
import com.baiyi.cratos.workorder.util.WorkflowUtils;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/18 11:18
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
public class WorkOrderTicketNodeFacadeImpl implements WorkOrderTicketNodeFacade {

    private final WorkOrderTicketNodeService workOrderTicketNodeService;

    @Override
    public void createWorkflowNodes(WorkOrder workOrder, WorkOrderTicket newTicket) {
        WorkflowModel.Workflow workflow = WorkflowUtils.loadAs(workOrder);
        if (CollectionUtils.isEmpty(workflow.getNodes())) {
            return;
        }
        List<WorkOrderTicketNode> nodes = Lists.newArrayList();
        workflow.getNodes()
                .forEach(e -> {
                    WorkOrderTicketNode ticketNode = WorkOrderTicketNode.builder()
                            .ticketId(newTicket.getId())
                            .approvalType(e.getApprovalType())
                            .nodeName(e.getName())
                            .parentId(CollectionUtils.isEmpty(nodes) ? 0 : nodes.getLast()
                                    .getId())
                            .comment(e.getComment())
                            .build();
                    workOrderTicketNodeService.add(ticketNode);
                    nodes.add(ticketNode);
                });
    }

    @Override
    public void verifyWorkflowNodes(WorkOrder workOrder, WorkOrderTicket workOrderTicket) {
        Map<String, WorkflowModel.Node> nodeMap = WorkflowUtils.toNodeMap(workOrder);
        List<WorkOrderTicketNode> nodes = workOrderTicketNodeService.queryByTicketId(workOrderTicket.getId());
        for (WorkOrderTicketNode node : nodes) {
            WorkflowModel.Node workflowNode = nodeMap.get(node.getNodeName());
            if (workflowNode == null) {
                WorkOrderTicketException.runtime("Work order verification failed: {} approval node not found!",
                        node.getNodeName());
            }
            if (ApprovalTypes.USER_SPECIFIED.name()
                    .equals(workflowNode.getApprovalType()) && StringUtils.isEmpty(node.getUsername())) {
                WorkOrderTicketException.runtime(
                        "Work order verification failed: {} Approval node must specify approver!", node.getNodeName());
            }
        }
    }

    @Override
    public void specifyNodeApprovalUser(int ticketId, String nodeName, String username) {
        WorkOrderTicketNode uk = WorkOrderTicketNode.builder()
                .ticketId(ticketId)
                .nodeName(nodeName)
                .username(username)
                .build();
        WorkOrderTicketNode workOrderTicketNode = workOrderTicketNodeService.getByUniqueKey(uk);
        if (Objects.isNull(workOrderTicketNode)) {
            return;
        }
        workOrderTicketNode.setUsername(username);
        workOrderTicketNodeService.updateByPrimaryKey(workOrderTicketNode);
    }

}
