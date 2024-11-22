package com.baiyi.cratos.facade.rbac.impl;

import com.baiyi.cratos.domain.generator.RbacRoleResource;
import com.baiyi.cratos.domain.param.http.rbac.RbacRoleResourceParam;
import com.baiyi.cratos.facade.rbac.RbacRoleResourceFacade;
import com.baiyi.cratos.service.RbacRoleResourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
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

    @Override
    public void copyRoleResource(RbacRoleResourceParam.CopyRoleResource copyRoleResource) {
        List<RbacRoleResource> rbacRoleResources = rbacRoleResourceService.queryByRoleId(copyRoleResource.getRoleId());
        if (CollectionUtils.isEmpty(rbacRoleResources)) {
            return;
        }
        rbacRoleResources.forEach(e -> {
            RbacRoleResource rbacRoleResource = RbacRoleResource.builder()
                    .resourceId(e.getResourceId())
                    .roleId(copyRoleResource.getTargetRoleId())
                    .build();
            if (rbacRoleResourceService.getByUniqueKey(rbacRoleResource) == null) {
                rbacRoleResourceService.add(rbacRoleResource);
            }
        });
    }

}
