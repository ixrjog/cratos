package com.baiyi.cratos.wrapper.work;

import com.baiyi.cratos.annotation.BusinessWrapper;
import com.baiyi.cratos.annotation.I18nWrapper;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.WorkOrderGroup;
import com.baiyi.cratos.domain.view.work.WorkOrderVO;
import com.baiyi.cratos.service.work.WorkOrderGroupService;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.BaseBusinessWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/17 14:21
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.WORKORDER_GROUP)
public class WorkOrderGroupWrapper extends BaseDataTableConverter<WorkOrderVO.Group, WorkOrderGroup> implements BaseBusinessWrapper<WorkOrderVO.HasWorkOrderGroup, WorkOrderVO.Group> {

    private final WorkOrderGroupService workOrderGroupService;

    @Override
    @BusinessWrapper(types = {BusinessTypeEnum.WORKORDER})
    @I18nWrapper
    public void wrap(WorkOrderVO.Group vo) {
        // This is a good idea
    }

    @Override
    public void businessWrap(WorkOrderVO.HasWorkOrderGroup hasWorkOrderGroup) {
        WorkOrderGroup group = workOrderGroupService.getById(hasWorkOrderGroup.getGroupId());
        if (Objects.nonNull(group)) {
            hasWorkOrderGroup.setWorkOrderGroup(wrapToTarget(group));
        }
    }

}