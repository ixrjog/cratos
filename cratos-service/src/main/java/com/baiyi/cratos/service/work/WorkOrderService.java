package com.baiyi.cratos.service.work;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.WorkOrder;
import com.baiyi.cratos.domain.param.http.work.WorkOrderParam;
import com.baiyi.cratos.mapper.WorkOrderMapper;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;
import com.baiyi.cratos.service.base.SupportBusinessService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/17 11:45
 * &#064;Version 1.0
 */
public interface WorkOrderService extends BaseUniqueKeyService<WorkOrder, WorkOrderMapper>, SupportBusinessService {

    List<WorkOrder> queryByGroupId(int groupId);

    default DataTable<WorkOrder> queryPageByParam(WorkOrderParam.WorkOrderPageQuery pageQuery) {
        Page<?> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<WorkOrder> data = getMapper().queryPageByParam(pageQuery);
        return new DataTable<>(data, page.getTotal());
    }

    default WorkOrder getByWorkOrderKey(String key) {
        return getByUniqueKey(WorkOrder.builder()
                .workOrderKey(key)
                .build());
    }

}
