package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.generator.RbacRoleMenu;
import com.baiyi.cratos.mapper.RbacRoleMenuMapper;
import com.baiyi.cratos.service.base.BaseService;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/4/8 上午9:53
 * @Version 1.0
 */
public interface RbacRoleMenuService extends BaseUniqueKeyService<RbacRoleMenu>, BaseService<RbacRoleMenu, RbacRoleMenuMapper> {

    List<Integer> queryUserMenuIds(String username);

    List<RbacRoleMenu> queryByRoleId(int roleId);

}
