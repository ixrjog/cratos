package com.baiyi.cratos.facade.work.impl;

import com.baiyi.cratos.common.util.VersionValidator;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.WorkOrder;
import com.baiyi.cratos.domain.generator.WorkOrderGroup;
import com.baiyi.cratos.domain.param.http.work.WorkOrderParam;
import com.baiyi.cratos.domain.view.work.WorkOrderVO;
import com.baiyi.cratos.facade.work.WorkOrderFacade;
import com.baiyi.cratos.service.work.WorkOrderGroupService;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.workorder.enums.WorkOrderGroupTypes;
import com.baiyi.cratos.workorder.exception.WorkOrderException;
import com.baiyi.cratos.wrapper.work.WorkOrderGroupWrapper;
import com.baiyi.cratos.wrapper.work.WorkOrderWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/17 14:07
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
public class WorkOrderFacadeImpl implements WorkOrderFacade {

    private final WorkOrderGroupService workOrderGroupService;
    private final WorkOrderService workOrderService;
    private final WorkOrderGroupWrapper workOrderGroupWrapper;
    private final WorkOrderWrapper workOrderWrapper;

    @Override
    public WorkOrderVO.Menu getWorkOrderMenu() {
        List<WorkOrderVO.Group> groupList = workOrderGroupService.selectAll()
                .stream()
                .map(workOrderGroupWrapper::wrapToTarget)
                .sorted(Comparator.comparing(WorkOrderVO.Group::getSeq))
                .toList();
        return WorkOrderVO.Menu.builder()
                .groupList(groupList)
                .build();
    }

    @Override
    public void updateWorkOrder(WorkOrderParam.UpdateWorkOrder updateWorkOrder) {
        if (!VersionValidator.isValidVersion(updateWorkOrder.getVersion())) {
            WorkOrderException.runtime("Version number format error.");
        }
        workOrderService.updateByPrimaryKey(updateWorkOrder.toTarget());
    }

    @Override
    public void updateWorkOrderGroup(WorkOrderParam.UpdateGroup updateGroup) {
        WorkOrderGroup workOrderGroup = updateGroup.toTarget();
        WorkOrderGroupTypes.valueOf(workOrderGroup.getGroupType());
        workOrderGroupService.updateByPrimaryKey(workOrderGroup);
    }

    @Override
    public DataTable<WorkOrderVO.Group> queryWorkOrderGroupPage(WorkOrderParam.GroupPageQuery pageQuery) {
        DataTable<WorkOrderGroup> table = workOrderGroupService.queryPageByParam(pageQuery);
        return workOrderGroupWrapper.wrapToTarget(table);
    }

    @Override
    public DataTable<WorkOrderVO.WorkOrder> queryWorkOrderPage(WorkOrderParam.WorkOrderPageQuery pageQuery) {
        DataTable<WorkOrder> table = workOrderService.queryPageByParam(pageQuery);
        return workOrderWrapper.wrapToTarget(table);
    }

}
