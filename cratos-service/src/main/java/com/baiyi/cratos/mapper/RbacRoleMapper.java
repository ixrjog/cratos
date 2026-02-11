package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.RbacRole;
import com.baiyi.cratos.domain.param.http.rbac.RbacRoleParam;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface RbacRoleMapper extends Mapper<RbacRole> {

    List<RbacRole> queryPageByParam(RbacRoleParam.RolePageQuery pageQuery);

}