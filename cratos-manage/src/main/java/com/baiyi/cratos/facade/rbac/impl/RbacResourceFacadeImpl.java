package com.baiyi.cratos.facade.rbac.impl;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.RbacResource;
import com.baiyi.cratos.domain.param.rbac.RbacResourceParam;
import com.baiyi.cratos.domain.view.rbac.RbacResourceVO;
import com.baiyi.cratos.facade.rbac.RbacResourceFacade;
import com.baiyi.cratos.service.RbacResourceService;
import com.baiyi.cratos.wrapper.RbacResourceWrapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/1/17 11:06
 * @Version 1.0
 */
@Slf4j
@Component
@AllArgsConstructor
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

}
