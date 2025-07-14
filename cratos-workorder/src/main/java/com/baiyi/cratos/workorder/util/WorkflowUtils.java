package com.baiyi.cratos.workorder.util;

import com.baiyi.cratos.domain.TicketWorkflow;
import com.baiyi.cratos.domain.YamlUtils;
import com.baiyi.cratos.domain.generator.WorkOrderTicketNode;
import com.baiyi.cratos.domain.model.WorkflowModel;
import com.google.gson.JsonSyntaxException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/18 10:57
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WorkflowUtils {

    public static WorkflowModel.Workflow loadAs(TicketWorkflow.HasWorkflow hasWorkflow) throws JsonSyntaxException {
        if (!org.springframework.util.StringUtils.hasText(hasWorkflow.getWorkflow())) {
            return WorkflowModel.Workflow.NO_DATA;
        }
        return YamlUtils.loadAs(hasWorkflow.getWorkflow(), WorkflowModel.Workflow.class);
    }

    public static void setWorkflow(TicketWorkflow.HasWorkflowData hasWorkflowData) {
        hasWorkflowData.setWorkflowData(loadAs(hasWorkflowData));
    }

    public static Map<String, WorkflowModel.Node> toNodeMap(TicketWorkflow.HasWorkflow hasWorkflow) {
        WorkflowModel.Workflow workflow = WorkflowUtils.loadAs(hasWorkflow);
        if (CollectionUtils.isEmpty(workflow.getNodes())) {
            return Map.of();
        }
        return workflow.getNodes()
                .stream()
                .collect(Collectors.toMap(WorkflowModel.Node::getName, node -> node,
                        (existing, replacement) -> existing));
    }

    public static Optional<WorkflowModel.Node> getNodeByName(WorkflowModel.Workflow workflow,
                                                             WorkOrderTicketNode ticketNode) {
        if (CollectionUtils.isEmpty(workflow.getNodes()) || ticketNode == null) {
            return Optional.empty();
        }
        return workflow.getNodes()
                .stream()
                .filter(node -> node.getName()
                        .equals(ticketNode.getNodeName()))
                .findFirst();
    }

}
