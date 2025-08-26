package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.RbacRoleResource;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface RbacRoleResourceMapper extends Mapper<RbacRoleResource> {

    List<Integer> queryResourceIds(int roleId);

}