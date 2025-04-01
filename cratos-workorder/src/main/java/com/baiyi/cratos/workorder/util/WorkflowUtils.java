package com.baiyi.cratos.workorder.util;

import com.baiyi.cratos.domain.HasWorkflow;
import com.baiyi.cratos.domain.YamlUtil;
import com.baiyi.cratos.domain.generator.WorkOrder;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.domain.model.WorkflowModel;
import com.google.gson.JsonSyntaxException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/18 10:57
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WorkflowUtils {


    public static WorkflowModel.Workflow loadAs(WorkOrderTicket ticket) throws JsonSyntaxException {
        if (!org.springframework.util.StringUtils.hasText(ticket.getWorkflow())) {
            return WorkflowModel.Workflow.NO_DATA;
        }
        return YamlUtil.loadAs(ticket.getWorkflow(), WorkflowModel.Workflow.class);
    }

    private static WorkflowModel.Workflow loadAs(WorkOrder workOrder) throws JsonSyntaxException {
        if (!org.springframework.util.StringUtils.hasText(workOrder.getWorkflow())) {
            return WorkflowModel.Workflow.NO_DATA;
        }
        return YamlUtil.loadAs(workOrder.getWorkflow(), WorkflowModel.Workflow.class);
    }

    public static WorkflowModel.Workflow loadAs(HasWorkflow hasWorkflow) throws JsonSyntaxException {
        if (!org.springframework.util.StringUtils.hasText(hasWorkflow.getWorkflow())) {
            return WorkflowModel.Workflow.NO_DATA;
        }
        return YamlUtil.loadAs(hasWorkflow.getWorkflow(), WorkflowModel.Workflow.class);
    }

    public static void setWorkflow(HasWorkflow hasWorkflow) {
        hasWorkflow.setWorkflowData(loadAs(hasWorkflow));
    }

    public static Map<String, WorkflowModel.Node> toNodeMap(WorkOrderTicket ticket) {
        WorkflowModel.Workflow workflow = WorkflowUtils.loadAs(ticket);
        if (CollectionUtils.isEmpty(workflow.getNodes())) {
            return Map.of();
        }
        return workflow.getNodes()
                .stream()
                .collect(Collectors.toMap(WorkflowModel.Node::getName, node -> node,
                        (existing, replacement) -> existing));
    }

    private static Map<String, WorkflowModel.Node> toNodeMap(WorkOrder workOrder) {
        WorkflowModel.Workflow workflow = WorkflowUtils.loadAs(workOrder);
        if (CollectionUtils.isEmpty(workflow.getNodes())) {
            return Map.of();
        }
        return workflow.getNodes()
                .stream()
                .collect(Collectors.toMap(WorkflowModel.Node::getName, node -> node,
                        (existing, replacement) -> existing));
    }

    public static Map<String, WorkflowModel.Node> toNodeMap(HasWorkflow hasWorkflow) {
        WorkflowModel.Workflow workflow = WorkflowUtils.loadAs(hasWorkflow);
        if (CollectionUtils.isEmpty(workflow.getNodes())) {
            return Map.of();
        }
        return workflow.getNodes()
                .stream()
                .collect(Collectors.toMap(WorkflowModel.Node::getName, node -> node,
                        (existing, replacement) -> existing));
    }

}
