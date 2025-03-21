package com.baiyi.cratos.wrapper.work;

import com.baiyi.cratos.annotation.BusinessWrapper;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.WorkOrder;
import com.baiyi.cratos.domain.model.WorkflowModel;
import com.baiyi.cratos.domain.util.BeanCopierUtil;
import com.baiyi.cratos.domain.view.user.UserVO;
import com.baiyi.cratos.domain.view.work.WorkOrderTicketVO;
import com.baiyi.cratos.service.BusinessTagService;
import com.baiyi.cratos.service.TagService;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.workorder.state.TicketState;
import com.baiyi.cratos.wrapper.base.IBaseWrapper;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/17 17:54
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.WORKORDER_TICKET_DETAILS)
public class WorkOrderTicketDetailsWrapper implements IBaseWrapper<WorkOrderTicketVO.TicketDetails> {

    private final WorkOrderService workOrderService;
    private final WorkOrderWrapper workOrderWrapper;
    private final TagService tagService;
    private final BusinessTagService businessTagService;
    private final UserService userService;

    @Override
    @BusinessWrapper(ofTypes = {BusinessTypeEnum.WORKORDER_TICKET, BusinessTypeEnum.WORKORDER_TICKET_ENTRY, BusinessTypeEnum.WORKORDER_TICKET_NODE}, invokeAt = BusinessWrapper.BEFORE)
    public void wrap(WorkOrderTicketVO.TicketDetails vo) {
        WorkOrder workOrder = workOrderService.getById(vo.getTicket()
                .getWorkOrderId());
        vo.setWorkOrder(workOrderWrapper.wrapToTarget(workOrder));
        //  Workflow
        WorkflowModel.Workflow workflow = vo.getWorkOrder()
                .getWorkflowData();
        if (TicketState.NEW.name()
                .equals(vo.getTicket()
                        .getTicketState())) {
            workflow.getNodes()
                    .forEach(node -> node.setSelectableUsers(querySelectableUsersByTags(node.getTags())));
        }
        vo.setWorkflow(workflow);
    }

    private List<UserVO.User> querySelectableUsersByTags(List<String> tags) {
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

    private UserVO.User getUser(int userId) {
        return BeanCopierUtil.copyProperties(userService.getById(userId), UserVO.User.class);
    }

}