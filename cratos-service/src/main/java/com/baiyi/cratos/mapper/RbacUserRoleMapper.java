package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.RbacUserRole;
import tk.mybatis.mapper.common.Mapper;

public interface RbacUserRoleMapper extends Mapper<RbacUserRole> {

    Integer queryUserMaxAccessLevel(String username);

}