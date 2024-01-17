package com.baiyi.cratos.facade.rbac;

import com.baiyi.cratos.domain.generator.RbacResource;

/**
 * @Author baiyi
 * @Date 2024/1/17 11:02
 * @Version 1.0
 */
public interface RbacResourceFacade {

    RbacResource getByResource(String resource);

}
