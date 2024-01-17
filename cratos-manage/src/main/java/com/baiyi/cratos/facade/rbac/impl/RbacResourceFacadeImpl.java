package com.baiyi.cratos.facade.rbac.impl;

import com.baiyi.cratos.domain.generator.RbacResource;
import com.baiyi.cratos.facade.rbac.RbacResourceFacade;
import com.baiyi.cratos.service.RbacResourceService;
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

    @Override
    public RbacResource getByResource(String resource) {
        RbacResource uniqueKey = RbacResource.builder()
                .resourceName(resource)
                .build();
        return rbacResourceService.getByUniqueKey(uniqueKey);
    }

}
