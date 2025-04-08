package com.baiyi.cratos.wrapper.work;

import com.baiyi.cratos.annotation.BusinessWrapper;
import com.baiyi.cratos.common.util.IdentityUtil;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.WorkOrder;
import com.baiyi.cratos.domain.util.I18nUtils;
import com.baiyi.cratos.domain.view.work.WorkOrderVO;
import com.baiyi.cratos.service.work.WorkOrderService;
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

    @Override
    @BusinessWrapper(ofTypes = {BusinessTypeEnum.WORKORDER_GROUP, BusinessTypeEnum.BUSINESS_DOC, BusinessTypeEnum.BUSINESS_TAG})
    public void wrap(WorkOrderVO.WorkOrder vo) {
        // I18n Data
        I18nUtils.setI18nData(vo);
        // Workflow Data
        WorkflowUtils.setWorkflow(vo);
    }

    @Override
    public void businessWrap(WorkOrderVO.HasWorkOrderList hasWorkOrderList) {
        if (IdentityUtil.hasIdentity(hasWorkOrderList.getGroupId())) {
            List<WorkOrderVO.WorkOrder> workOrderList = workOrderService.queryByGroupId(hasWorkOrderList.getGroupId())
                    .stream()
                    .map(this::wrapToTarget)
                    .sorted(Comparator.comparing(WorkOrderVO.WorkOrder::getSeq))
                    .toList();
            hasWorkOrderList.setWorkOrderList(workOrderList);
        }
    }

}