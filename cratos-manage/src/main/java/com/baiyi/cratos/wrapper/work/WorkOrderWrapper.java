package com.baiyi.cratos.wrapper.work;

import com.baiyi.cratos.annotation.BusinessWrapper;
import com.baiyi.cratos.annotation.I18nWrapper;
import com.baiyi.cratos.common.enums.AccessLevel;
import com.baiyi.cratos.common.util.IdentityUtils;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.WorkOrder;
import com.baiyi.cratos.domain.generator.WorkOrderGroup;
import com.baiyi.cratos.domain.view.work.WorkOrderVO;
import com.baiyi.cratos.facade.RbacRoleFacade;
import com.baiyi.cratos.service.work.WorkOrderGroupService;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.workorder.enums.WorkOrderStatus;
import com.baiyi.cratos.workorder.util.WorkflowUtils;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.IBusinessWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/17 14:37
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.WORKORDER)
public class WorkOrderWrapper extends BaseDataTableConverter<WorkOrderVO.WorkOrder, WorkOrder> implements IBusinessWrapper<WorkOrderVO.HasWorkOrderList, WorkOrderVO.WorkOrder> {

    private final WorkOrderService workOrderService;
    private final WorkOrderGroupService workOrderGroupService;
    private final RbacRoleFacade rbacRoleFacade;
    private final WorkOrderGroupWrapper workOrderGroupWrapper;

    @Override
    @BusinessWrapper(types = {BusinessTypeEnum.BUSINESS_DOC, BusinessTypeEnum.BUSINESS_TAG})
    @I18nWrapper
    public void wrap(WorkOrderVO.WorkOrder vo) {
        // Workflow Data
        WorkflowUtils.setWorkflow(vo);
        if (WorkOrderStatus.DEVELOPING.equals(WorkOrderStatus.valueOf(vo.getStatus()))) {
            vo.setIsUsable(rbacRoleFacade.verifyRoleAccessLevelByUsername(AccessLevel.OPS));
        } else {
            vo.setIsUsable(true);
        }
        WorkOrderGroup workOrderGroup = workOrderGroupService.getById(vo.getGroupId());
        vo.setWorkOrderGroup(workOrderGroupWrapper.convert(workOrderGroup));
    }

    @Override
    public void businessWrap(WorkOrderVO.HasWorkOrderList hasWorkOrderList) {
        if (IdentityUtils.hasIdentity(hasWorkOrderList.getGroupId())) {
            List<WorkOrderVO.WorkOrder> workOrderList = workOrderService.queryByGroupId(hasWorkOrderList.getGroupId())
                    .stream()
                    .map(this::wrapToTarget)
                    // 过滤无效的工单
                    .filter(WorkOrderVO.WorkOrder::getValid)
                    .sorted(Comparator.comparing(WorkOrderVO.WorkOrder::getSeq))
                    .toList();
            hasWorkOrderList.setWorkOrderList(workOrderList);
        }
    }

}