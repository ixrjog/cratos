package com.baiyi.cratos.facade.work.impl;

import com.baiyi.cratos.common.util.PasswordGenerator;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.*;
import com.baiyi.cratos.domain.model.WorkflowModel;
import com.baiyi.cratos.workorder.facade.WorkOrderTicketSubscriberFacade;
import com.baiyi.cratos.service.BusinessTagService;
import com.baiyi.cratos.service.TagService;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.service.work.WorkOrderTicketNodeService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.service.work.WorkOrderTicketSubscriberService;
import com.baiyi.cratos.workorder.enums.ApprovalTypes;
import com.baiyi.cratos.workorder.enums.SubscribeStatus;
import com.baiyi.cratos.workorder.util.WorkflowUtils;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/18 17:04
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
public class WorkOrderTicketSubscriberFacadeImpl implements WorkOrderTicketSubscriberFacade {

    private final WorkOrderService workOrderService;
    private final WorkOrderTicketService workOrderTicketService;
    private final WorkOrderTicketNodeService workOrderTicketNodeService;
    private final WorkOrderTicketSubscriberService workOrderTicketSubscriberService;
    private final UserService userService;
    private final TagService tagService;
    private final BusinessTagService businessTagService;

    @Override
    public void publish(WorkOrderTicket ticket, User user) {
        createSubscriber(ticket, user, SubscribeStatus.APPLICANT);
    }

    @Override
    public void publish(WorkOrderTicket ticket) {
        WorkOrder workOrder = workOrderService.getById(ticket.getWorkOrderId());
        Map<String, WorkflowModel.Node> nodeMap = WorkflowUtils.toNodeMap(workOrder);
        List<WorkOrderTicketNode> ticketNodes = workOrderTicketNodeService.queryByTicketId(ticket.getId());
        ticketNodes.forEach(n -> {
            if (nodeMap.containsKey(n.getNodeName())) {
                WorkflowModel.Node workflowNode = nodeMap.get(n.getNodeName());
                if (ApprovalTypes.OR_BATCH.name()
                        .equals(workflowNode.getApprovalType())) {
                    // 广播
                    List<User> approvers = queryApprovers(workflowNode.getTags());
                    approvers.forEach(approver -> createSubscriber(ticket, approver, SubscribeStatus.APPROVED_BY));
                    return;
                }
                if (ApprovalTypes.USER_SPECIFIED.name()
                        .equals(workflowNode.getApprovalType())) {
                    // 单播
                    User auditUser = userService.getByUsername(n.getUsername());
                    createSubscriber(ticket, auditUser, SubscribeStatus.APPROVED_BY);
                }
            }
        });
    }

    private List<User> queryApprovers(List<String> tags) {
        if (CollectionUtils.isEmpty(tags)) {
            return List.of();
        }
        Map<String, User> userMap = Maps.newHashMap();
        tags.stream()
                .map(tagService::getByTagKey)
                .filter(Objects::nonNull)
                .forEach(
                        tag -> businessTagService.queryByBusinessTypeAndTagId(BusinessTypeEnum.USER.name(), tag.getId())
                                .forEach(businessTag -> {
                                    User user = userService.getById(businessTag.getBusinessId());
                                    userMap.put(user.getUsername(), user);
                                }));
        return new ArrayList<>(userMap.values());
    }

    private void createSubscriber(WorkOrderTicket ticket, User user, SubscribeStatus constants) {
        // 生成128位Token
        String token = PasswordGenerator.generatePassword(128, true, true, true, false);
        WorkOrderTicketSubscriber subscriber = WorkOrderTicketSubscriber.builder()
                .ticketId(ticket.getId())
                .username(user.getUsername())
                .subscribeStatus(constants.name())
                .valid(true)
                .token(token)
                .build();
        workOrderTicketSubscriberService.add(subscriber);
    }

}
