package com.baiyi.cratos.facade.rbac.impl;

import com.baiyi.cratos.domain.generator.RbacRoleMenu;
import com.baiyi.cratos.domain.param.rbac.RbacRoleMenuParam;
import com.baiyi.cratos.facade.rbac.RbacRoleMenuFacade;
import com.baiyi.cratos.service.RbacRoleMenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/4/8 下午3:18
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RbacRoleMenuFacadeImpl implements RbacRoleMenuFacade {

    private final RbacRoleMenuService rbacRoleMenuService;

    @Override
    public void addRoleMenu(RbacRoleMenuParam.AddRoleMenu addRoleMenu) {
        RbacRoleMenu rbacRoleMenu = addRoleMenu.toTarget();
        if (rbacRoleMenuService.getByUniqueKey(rbacRoleMenu) == null) {
            rbacRoleMenuService.add(rbacRoleMenu);
        }
    }

    @Override
    public void deleteById(int id) {
        rbacRoleMenuService.deleteById(id);
    }

}
