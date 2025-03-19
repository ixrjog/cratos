package com.baiyi.cratos.facade.work.impl;

import com.baiyi.cratos.domain.view.work.WorkOrderVO;
import com.baiyi.cratos.facade.work.WorkOrderFacade;
import com.baiyi.cratos.service.work.WorkOrderGroupService;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.wrapper.work.WorkOrderGroupWrapper;
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

}
