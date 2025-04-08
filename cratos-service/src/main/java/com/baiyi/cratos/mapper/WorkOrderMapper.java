package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.WorkOrder;
import com.baiyi.cratos.domain.param.http.work.WorkOrderParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface WorkOrderMapper extends Mapper<WorkOrder> {

    List<WorkOrder> queryPageByParam(WorkOrderParam.WorkOrderPageQuery pageQuery);

}