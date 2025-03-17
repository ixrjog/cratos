package com.baiyi.cratos.service.work;

import com.baiyi.cratos.domain.generator.WorkOrderGroup;
import com.baiyi.cratos.mapper.WorkOrderGroupMapper;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/17 13:39
 * &#064;Version 1.0
 */
public interface WorkOrderGroupService extends BaseUniqueKeyService<WorkOrderGroup, WorkOrderGroupMapper> {

    default WorkOrderGroup getByName(String name) {
        return getByUniqueKey(WorkOrderGroup.builder()
                .name(name)
                .build());
    }

}
