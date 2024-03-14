package com.baiyi.cratos.facade.rbac.impl;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.RbacResource;
import com.baiyi.cratos.domain.param.rbac.RbacResourceParam;
import com.baiyi.cratos.domain.view.rbac.RbacResourceVO;
import com.baiyi.cratos.facade.rbac.RbacResourceFacade;
import com.baiyi.cratos.service.RbacResourceService;
import com.baiyi.cratos.wrapper.RbacResourceWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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

}
