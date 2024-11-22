package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.RbacResource;
import com.baiyi.cratos.domain.param.http.rbac.RbacResourceParam;
import com.baiyi.cratos.domain.param.http.rbac.RbacRoleResourceParam;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface RbacResourceMapper extends Mapper<RbacResource> {

    List<RbacResource> queryPageByParam(RbacResourceParam.ResourcePageQuery pageQuery);

    List<RbacResource> queryResourceInRoleByParam(RbacRoleResourceParam.RoleResourcePageQuery pageQuery);

    List<RbacResource> queryResourceNotInRoleByParam(RbacRoleResourceParam.RoleResourcePageQuery pageQuery);

    int countResourcesAuthorizedByUsername(@Param("username") String username, @Param("resource") String resource);

}