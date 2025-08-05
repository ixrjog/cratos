package com.baiyi.cratos.wrapper.work;

import com.baiyi.cratos.annotation.BusinessWrapper;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.WorkOrder;
import com.baiyi.cratos.domain.generator.WorkOrderTicketNode;
import com.baiyi.cratos.domain.model.WorkflowModel;
import com.baiyi.cratos.domain.util.BeanCopierUtils;
import com.baiyi.cratos.domain.view.user.UserVO;
import com.baiyi.cratos.domain.view.work.WorkOrderTicketVO;
import com.baiyi.cratos.service.BusinessTagService;
import com.baiyi.cratos.service.TagService;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.service.work.WorkOrderTicketNodeService;
import com.baiyi.cratos.workorder.enums.TicketState;
import com.baiyi.cratos.workorder.facade.TicketWorkflowFacade;
import com.baiyi.cratos.wrapper.base.IBaseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

import static com.baiyi.cratos.annotation.BusinessWrapper.InvokeAts.BEFORE;

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
    private final TicketWorkflowFacade workflowFacade;
    private final WorkOrderTicketNodeService workOrderTicketNodeService;

    @Override
    @BusinessWrapper(ofTypes = {BusinessTypeEnum.WORKORDER_TICKET, BusinessTypeEnum.WORKORDER_TICKET_ENTRY, BusinessTypeEnum.WORKORDER_TICKET_NODE}, invokeAt = BEFORE)
    public void wrap(WorkOrderTicketVO.TicketDetails vo) {
        WorkOrder workOrder = workOrderService.getById(vo.getTicket()
                .getWorkOrderId());
        vo.setWorkOrder(workOrderWrapper.wrapToTarget(workOrder));
        //  Workflow
        WorkflowModel.Workflow workflow = vo.getTicket()
                .getWorkflowData();
        // 审批中
        if (TicketState.IN_APPROVAL.name()
                .equals(vo.getTicket()
                        .getTicketState())) {
            if (vo.getTicket()
                    .getNodeId() != 0) {
                WorkOrderTicketNode ticketNode = workOrderTicketNodeService.getById(vo.getTicket()
                        .getNodeId());
                if (Objects.nonNull(ticketNode)) {
                    vo.setCurrentNode(ticketNode.getNodeName());
                }
            }
        }
        if (!CollectionUtils.isEmpty(workflow.getNodes())) {
            workflow.getNodes()
                    .forEach(node -> {
                        List<UserVO.User> selectableUsers = BeanCopierUtils.copyListProperties(
                                workflowFacade.querySelectableUsersByTags(node.getTags()), UserVO.User.class);
                        node.setSelectableUsers(selectableUsers);
                    });
        }
        vo.setWorkflow(workflow);
    }

}