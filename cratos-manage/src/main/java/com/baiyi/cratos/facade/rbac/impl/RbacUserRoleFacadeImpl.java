package com.baiyi.cratos.facade.rbac.impl;

import com.baiyi.cratos.domain.generator.RbacUserRole;
import com.baiyi.cratos.domain.param.rbac.RbacUserRoleParam;
import com.baiyi.cratos.facade.rbac.RbacUserRoleFacade;
import com.baiyi.cratos.service.RbacUserRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/3/18 16:06
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RbacUserRoleFacadeImpl implements RbacUserRoleFacade {

    private final RbacUserRoleService rbacUserRoleService;

    @Override
    public void addUserRole(RbacUserRoleParam.AddUserRole addUserRole) {
        RbacUserRole rbacUserRole = addUserRole.toTarget();
        if (rbacUserRoleService.getByUniqueKey(rbacUserRole) == null) {
            rbacUserRoleService.add(rbacUserRole);
        }
    }

    @Override
    public void deleteUserRole(RbacUserRoleParam.DeleteUserRole deleteUserRole) {
        RbacUserRole rbacUserRole = deleteUserRole.toTarget();
        RbacUserRole dbRbacUserRole = rbacUserRoleService.getByUniqueKey(rbacUserRole);
        if (dbRbacUserRole != null) {
            rbacUserRoleService.deleteById(dbRbacUserRole.getId());
        }
    }

}
