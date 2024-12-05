package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.generator.RbacUserRole;
import com.baiyi.cratos.mapper.RbacUserRoleMapper;
import com.baiyi.cratos.service.base.BaseService;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/1/22 17:26
 * @Version 1.0
 */
public interface RbacUserRoleService extends BaseUniqueKeyService<RbacUserRole, RbacUserRoleMapper>, BaseService<RbacUserRole, RbacUserRoleMapper> {

    List<RbacUserRole> queryByUsername(String username);

    int selectCountByUsername(String username);

    int selectCountByRoleId(int roleId);

    int queryUserMaxAccessLevel(String username);

}
