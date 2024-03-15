package com.baiyi.cratos.facade.rbac.impl;

import com.baiyi.cratos.domain.param.rbac.RbacRoleResourceParam;
import com.baiyi.cratos.facade.rbac.RbacRoleResourceFacade;
import com.baiyi.cratos.service.RbacRoleResourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

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
        addRoleResource.toRbacRoleResources()
                .stream()
                .filter(rbacRoleResource -> rbacRoleResourceService.getByUniqueKey(rbacRoleResource) == null)
                .forEach(rbacRoleResourceService::add);
    }

    @Override
    public void deleteRoleResource(RbacRoleResourceParam.DeleteRoleResource deleteRoleResource) {
        deleteRoleResource.toRbacRoleResources()
                .stream()
                .map(rbacRoleResourceService::getByUniqueKey)
                .filter(Objects::nonNull)
                .forEach(dbRbacRoleResource -> rbacRoleResourceService.deleteById(dbRbacRoleResource.getId()));
    }

}
