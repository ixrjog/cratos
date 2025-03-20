package com.baiyi.cratos.wrapper.work;

import com.baiyi.cratos.annotation.BusinessWrapper;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.WorkOrder;
import com.baiyi.cratos.domain.model.WorkflowModel;
import com.baiyi.cratos.domain.view.work.WorkOrderTicketVO;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.wrapper.base.IBaseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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

    @Override
    @BusinessWrapper(ofTypes = {BusinessTypeEnum.WORKORDER_TICKET, BusinessTypeEnum.WORKORDER_TICKET_ENTRY, BusinessTypeEnum.WORKORDER_TICKET_NODE}, invokeAt = BusinessWrapper.BEFORE)
    public void wrap(WorkOrderTicketVO.TicketDetails vo) {
        WorkOrder workOrder = workOrderService.getById(vo.getTicket()
                .getWorkOrderId());
        vo.setWorkOrder(workOrderWrapper.wrapToTarget(workOrder));
        //  Workflow
        WorkflowModel.Workflow workflow = vo.getWorkOrder()
                .getWorkflowData();

        vo.setWorkflow(workflow);
    }

}