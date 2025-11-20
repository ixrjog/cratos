package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.RbacRole;
import com.baiyi.cratos.domain.param.http.rbac.RbacRoleParam;
import com.baiyi.cratos.mapper.RbacRoleMapper;
import com.baiyi.cratos.service.base.BaseService;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;

/**
 * @Author baiyi
 * @Date 2024/1/17 17:25
 * @Version 1.0
 */
public interface RbacRoleService extends BaseUniqueKeyService<RbacRole, RbacRoleMapper>,BaseService<RbacRole, RbacRoleMapper> {

    DataTable<RbacRole> queryPageByParam(RbacRoleParam.RolePageQuery pageQuery);

    RbacRole getByRoleName(String roleName);

}
