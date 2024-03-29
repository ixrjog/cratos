package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.generator.RbacRoleResource;
import com.baiyi.cratos.mapper.RbacRoleResourceMapper;
import com.baiyi.cratos.service.base.BaseService;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;

/**
 * @Author baiyi
 * @Date 2024/3/15 10:18
 * @Version 1.0
 */
public interface RbacRoleResourceService extends BaseUniqueKeyService<RbacRoleResource>, BaseService<RbacRoleResource, RbacRoleResourceMapper> {

    int selectCountByRoleId(int roleId);

}