package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.generator.UserPermission;
import com.baiyi.cratos.mapper.UserPermissionMapper;
import com.baiyi.cratos.service.base.BaseService;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;

/**
 * @Author baiyi
 * @Date 2024/1/18 17:33
 * @Version 1.0
 */
public interface UserPermissionService extends BaseUniqueKeyService<UserPermission>, BaseService<UserPermission, UserPermissionMapper> {

}