package com.baiyi.cratos.service.work;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.WorkOrderGroup;
import com.baiyi.cratos.domain.param.http.work.WorkOrderParam;
import com.baiyi.cratos.mapper.WorkOrderGroupMapper;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/17 13:39
 * &#064;Version 1.0
 */
public interface WorkOrderGroupService extends BaseUniqueKeyService<WorkOrderGroup, WorkOrderGroupMapper> {

    default DataTable<WorkOrderGroup> queryPageByParam(WorkOrderParam.GroupPageQuery pageQuery) {
        Page<?> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<WorkOrderGroup> data = getMapper().queryPageByParam(pageQuery);
        return new DataTable<>(data, page.getTotal());
    }

    default WorkOrderGroup getByName(String name) {
        return getByUniqueKey(WorkOrderGroup.builder()
                .name(name)
                .build());
    }

}
