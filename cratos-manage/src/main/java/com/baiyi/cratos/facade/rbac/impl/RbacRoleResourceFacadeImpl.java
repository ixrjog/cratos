package com.baiyi.cratos.facade.rbac.impl;

import com.baiyi.cratos.domain.generator.RbacRoleResource;
import com.baiyi.cratos.domain.param.rbac.RbacRoleResourceParam;
import com.baiyi.cratos.facade.rbac.RbacRoleResourceFacade;
import com.baiyi.cratos.service.RbacRoleResourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/3/15 15:54
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RbacRoleResourceFacadeImpl implements RbacRoleResourceFacade {

    private final RbacRoleResourceService rbacRoleResourceService;

    @Override
    public void addRoleResource(RbacRoleResourceParam.AddRoleResource addRoleResource) {
        RbacRoleResource rbacRoleResource = addRoleResource.toTarget();
        if (rbacRoleResourceService.getByUniqueKey(rbacRoleResource) == null) {
            rbacRoleResourceService.add(rbacRoleResource);
        }
    }

    @Override
    public void deleteRoleResource(RbacRoleResourceParam.DeleteRoleResource deleteRoleResource) {
        RbacRoleResource rbacRoleResource = deleteRoleResource.toTarget();
        RbacRoleResource dbRbacRoleResource = rbacRoleResourceService.getByUniqueKey(rbacRoleResource);
        if (dbRbacRoleResource != null) {
            rbacRoleResourceService.deleteById(dbRbacRoleResource.getId());
        }
    }

}
