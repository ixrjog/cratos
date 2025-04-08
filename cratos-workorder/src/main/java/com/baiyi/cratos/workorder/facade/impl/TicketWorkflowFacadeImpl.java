package com.baiyi.cratos.workorder.facade.impl;

import com.baiyi.cratos.domain.TicketWorkflow;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.domain.generator.WorkOrderTicketNode;
import com.baiyi.cratos.domain.view.work.WorkOrderTicketVO;
import com.baiyi.cratos.service.BusinessTagService;
import com.baiyi.cratos.service.TagService;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.workorder.enums.ApprovalTypes;
import com.baiyi.cratos.workorder.facade.TicketWorkflowFacade;
import com.baiyi.cratos.workorder.util.WorkflowUtils;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/24 11:43
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
public class TicketWorkflowFacadeImpl implements TicketWorkflowFacade {

    private final WorkOrderService workOrderService;
    private final TagService tagService;
    private final BusinessTagService businessTagService;
    private final UserService userService;

    @Override
    public List<User> querySelectableUsersByTags(List<String> tags) {
        if (CollectionUtils.isEmpty(tags)) {
            return List.of();
        }
        Map<Integer, User> userMap = Maps.newHashMap();
        tags.stream()
                .map(tagService::getByTagKey)
                .filter(Objects::nonNull)
                .forEach(
                        tag -> businessTagService.queryByBusinessTypeAndTagId(BusinessTypeEnum.USER.name(), tag.getId())
                                .forEach(businessTag -> userMap.computeIfAbsent(businessTag.getBusinessId(),
                                        this::getUser)));
        return new ArrayList<>(userMap.values());
    }

    @Override
    public List<User> queryNodeApprovalUsers(WorkOrderTicket ticket, String nodeName) {
        return queryNodeApprovalUsersByWorkflow(ticket, nodeName);
    }

    private List<User> queryNodeApprovalUsersByWorkflow(TicketWorkflow.HasWorkflow hasWorkflow, String nodeName) {
        return WorkflowUtils.loadAs(hasWorkflow)
                .getNodes()
                .stream()
                .filter(node -> node.getName()
                        .equals(nodeName))
                .findFirst()
                .map(node -> querySelectableUsersByTags(node.getTags()))
                .orElse(List.of());
    }

    @Override
    public List<String> queryNodeApprovalUsernames(WorkOrderTicket ticket, String nodeName) {
        return WorkflowUtils.loadAs(ticket)
                .getNodes()
                .stream()
                .filter(node -> node.getName()
                        .equals(nodeName))
                .findFirst()
                .map(node -> querySelectableUsernameByTags(node.getTags()))
                .orElse(List.of());
    }

    private List<String> querySelectableUsernameByTags(List<String> tags) {
        if (CollectionUtils.isEmpty(tags)) {
            return List.of();
        }
        Map<Integer, String> usernameMap = Maps.newHashMap();
        tags.stream()
                .map(tagService::getByTagKey)
                .filter(Objects::nonNull)
                .forEach(
                        tag -> businessTagService.queryByBusinessTypeAndTagId(BusinessTypeEnum.USER.name(), tag.getId())
                                .forEach(businessTag -> usernameMap.computeIfAbsent(businessTag.getBusinessId(),
                                        this::getUsername)));
        return new ArrayList<>(usernameMap.values());
    }

    @Override
    public boolean isApprover(WorkOrderTicket ticket, String nodeName, String username) {
        return queryNodeApprovalUsers(ticket, nodeName).stream()
                .anyMatch(e -> e.getUsername()
                        .equals(username));
    }

    private boolean isApprover(WorkOrderTicketVO.Ticket ticket, String nodeName, String username) {
        return queryNodeApprovalUsersByWorkflow(ticket, nodeName).stream()
                .anyMatch(e -> e.getUsername()
                        .equals(username));
    }

    @Override
    public boolean isApprover(WorkOrderTicketVO.Ticket ticket, WorkOrderTicketNode ticketNode, String username) {
        // 用户指定审批人
        if (ApprovalTypes.USER_SPECIFIED.equals(ApprovalTypes.valueOf(ticketNode.getApprovalType()))) {
            return StringUtils.hasText(username) && username.equals(ticketNode.getUsername());
        }
        // 或批
        if (ApprovalTypes.OR_BATCH.equals(ApprovalTypes.valueOf(ticketNode.getApprovalType()))) {
            return isApprover(ticket, ticketNode.getNodeName(), username);
        }
        return false;
    }

    private String getUsername(int userId) {
        return userService.getById(userId)
                .getUsername();
    }

    private User getUser(int userId) {
        return userService.getById(userId);
    }

}
