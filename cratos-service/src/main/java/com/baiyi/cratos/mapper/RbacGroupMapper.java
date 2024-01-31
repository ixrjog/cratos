package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.RbacGroup;
import com.baiyi.cratos.domain.param.rbac.RbacGroupParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface RbacGroupMapper extends Mapper<RbacGroup> {

    List<RbacGroup> queryPageByParam(RbacGroupParam.GroupPageQuery pageQuery);

}