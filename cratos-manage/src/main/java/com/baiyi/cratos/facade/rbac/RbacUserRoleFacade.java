package com.baiyi.cratos.facade.rbac;

import com.baiyi.cratos.domain.generator.RbacUserRole;
import com.baiyi.cratos.domain.param.http.rbac.RbacUserRoleParam;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/3/18 16:05
 * @Version 1.0
 */
public interface RbacUserRoleFacade {

    void addUserRole(RbacUserRoleParam.AddUserRole addUserRole);

    void deleteUserRole(RbacUserRoleParam.DeleteUserRole deleteUserRole);

    List<RbacUserRole> queryUserRoles(String username);

}
