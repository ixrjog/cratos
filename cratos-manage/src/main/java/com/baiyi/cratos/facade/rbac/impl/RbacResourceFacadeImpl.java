package com.baiyi.cratos.facade.rbac.impl;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.RbacResource;
import com.baiyi.cratos.domain.generator.RbacRoleResource;
import com.baiyi.cratos.domain.param.rbac.RbacResourceParam;
import com.baiyi.cratos.domain.param.rbac.RbacRoleResourceParam;
import com.baiyi.cratos.domain.view.rbac.RbacResourceVO;
import com.baiyi.cratos.facade.rbac.RbacResourceFacade;
import com.baiyi.cratos.service.RbacResourceService;
import com.baiyi.cratos.service.RbacRoleResourceService;
import com.baiyi.cratos.wrapper.RbacResourceWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/1/17 11:06
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RbacResourceFacadeImpl implements RbacResourceFacade {

    private final RbacResourceService rbacResourceService;
    private final RbacResourceWrapper rbacResourceWrapper;
    private final RbacRoleResourceService rbacRoleResourceService;

    @Override
    public RbacResource getByResource(String resource) {
        RbacResource uniqueKey = RbacResource.builder()
                .resourceName(resource)
                .build();
        return rbacResourceService.getByUniqueKey(uniqueKey);
    }

    @Override
    public DataTable<RbacResourceVO.Resource> queryResourcePage(RbacResourceParam.ResourcePageQuery pageQuery) {
        DataTable<RbacResource> table = rbacResourceService.queryPageByParam(pageQuery);
        return rbacResourceWrapper.wrapToTarget(table);
    }

    @Override
    public void addResource(RbacResourceParam.AddResource addResource) {
        RbacResource rbacResource = addResource.toTarget();
        if (rbacResourceService.getByUniqueKey(rbacResource) == null) {
            rbacResourceService.add(rbacResource);
        }
    }

    @Override
    public void updateResource(RbacResourceParam.UpdateResource updateResource) {
        RbacResource rbacResource = rbacResourceService.getById(updateResource.getId());
        if (rbacResource != null) {
            rbacResource.setUiPoint(updateResource.getUiPoint());
            rbacResource.setGroupId(updateResource.getGroupId());
            rbacResource.setValid(updateResource.getValid());
            rbacResource.setComment(updateResource.getComment());
            rbacResourceService.updateByPrimaryKey(rbacResource);
        }
    }

    @Override
    public void setResourceValidById(int id) {
        rbacResourceService.updateValidById(id);
    }

    @Override
    public DataTable<RbacResourceVO.Resource> queryRoleResourcePage(
            RbacRoleResourceParam.RoleResourcePageQuery pageQuery) {
        DataTable<RbacResource> table = rbacResourceService.queryRoleResourcePageByParam(pageQuery);
        return rbacResourceWrapper.wrapToTarget(table);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void deleteResourceById(int id) {
        List<RbacRoleResource> rbacRoleResources = rbacRoleResourceService.queryByResourceId(id);
        if (!CollectionUtils.isEmpty(rbacRoleResources)) {
            for (RbacRoleResource rbacRoleResource : rbacRoleResources) {
                rbacRoleResourceService.deleteById(rbacRoleResource.getId());
            }
        }
        rbacResourceService.deleteById(id);
    }

}
