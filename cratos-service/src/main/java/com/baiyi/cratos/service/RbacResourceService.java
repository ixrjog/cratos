package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.generator.RbacResource;
import com.baiyi.cratos.mapper.RbacResourceMapper;
import com.baiyi.cratos.service.base.BaseService;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;

/**
 * @Author baiyi
 * @Date 2024/1/17 11:07
 * @Version 1.0
 */
public interface RbacResourceService extends BaseUniqueKeyService<RbacResource>, BaseService<RbacResource, RbacResourceMapper> {
}