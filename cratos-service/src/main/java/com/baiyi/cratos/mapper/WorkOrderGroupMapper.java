package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.WorkOrderGroup;
import com.baiyi.cratos.domain.param.http.work.WorkOrderParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface WorkOrderGroupMapper extends Mapper<WorkOrderGroup> {

    List<WorkOrderGroup> queryPageByParam(WorkOrderParam.GroupPageQuery pageQuery);

}