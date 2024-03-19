package com.baiyi.cratos.facade.rbac;

import com.baiyi.cratos.domain.param.rbac.RbacUserRoleParam;

/**
 * @Author baiyi
 * @Date 2024/3/18 16:05
 * @Version 1.0
 */
public interface RbacUserRoleFacade {

    void addUserRole(RbacUserRoleParam.AddUserRole addUserRole);

    void deleteUserRole(RbacUserRoleParam.DeleteUserRole deleteUserRole);

}
