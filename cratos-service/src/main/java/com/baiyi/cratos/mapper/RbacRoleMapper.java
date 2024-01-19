package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.RbacRole;
import com.baiyi.cratos.domain.param.rbac.RbacRoleParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface RbacRoleMapper extends Mapper<RbacRole> {

    List<RbacRole> queryPageByParam(RbacRoleParam.RolePageQuery pageQuery);

}