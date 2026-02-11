package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.WorkOrderGroup;
import com.baiyi.cratos.domain.param.http.work.WorkOrderParam;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface WorkOrderGroupMapper extends Mapper<WorkOrderGroup> {

    List<WorkOrderGroup> queryPageByParam(WorkOrderParam.GroupPageQuery pageQuery);

}