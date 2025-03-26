package com.baiyi.cratos.workorder.facade.impl;

import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.WorkOrder;
import com.baiyi.cratos.domain.util.BeanCopierUtil;
import com.baiyi.cratos.domain.view.user.UserVO;
import com.baiyi.cratos.service.BusinessTagService;
import com.baiyi.cratos.service.TagService;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.workorder.facade.TicketWorkflowFacade;
import com.baiyi.cratos.workorder.util.WorkflowUtils;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

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
    public List<UserVO.User> querySelectableUsersByTags(List<String> tags) {
        if (CollectionUtils.isEmpty(tags)) {
            return List.of();
        }
        Map<Integer, UserVO.User> userMap = Maps.newHashMap();
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
    public List<UserVO.User> queryNodeApprovalUsers(WorkOrder workOrder, String nodeName) {
        return WorkflowUtils.loadAs(workOrder)
                .getNodes()
                .stream()
                .filter(node -> node.getName()
                        .equals(nodeName))
                .findFirst()
                .map(node -> querySelectableUsersByTags(node.getTags()))
                .orElse(List.of());
    }

    @Override
    public List<String> queryNodeApprovalUsernames(WorkOrder workOrder, String nodeName) {
        return WorkflowUtils.loadAs(workOrder)
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
    public boolean isApprover(WorkOrder workOrder, String nodeName, String username) {
        return queryNodeApprovalUsers(workOrder, nodeName).stream()
                .anyMatch(e -> e.getUsername()
                        .equals(username));
    }

    private String getUsername(int userId) {
        return userService.getById(userId).getUsername();
    }


    private UserVO.User getUser(int userId) {
        return BeanCopierUtil.copyProperties(userService.getById(userId), UserVO.User.class);
    }

}
