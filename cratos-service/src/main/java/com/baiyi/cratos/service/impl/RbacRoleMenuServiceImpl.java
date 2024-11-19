package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.domain.generator.RbacRoleMenu;
import com.baiyi.cratos.mapper.RbacRoleMenuMapper;
import com.baiyi.cratos.service.RbacRoleMenuService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

import static com.baiyi.cratos.common.configuration.CachingConfiguration.RepositoryName.LONG_TERM;

/**
 * @Author baiyi
 * @Date 2024/4/8 上午9:57
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class RbacRoleMenuServiceImpl implements RbacRoleMenuService {

    private final RbacRoleMenuMapper rbacRoleMenuMapper;

    @Override
    public RbacRoleMenu getByUniqueKey(@NonNull RbacRoleMenu record) {
        Example example = new Example(RbacRoleMenu.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("roleId", record.getRoleId())
                .andEqualTo("menuId", record.getMenuId());
        return rbacRoleMenuMapper.selectOneByExample(example);
    }

    @Override
    public List<Integer> queryUserMenuIds(String username) {
        return rbacRoleMenuMapper.queryUserMenuIds(username);
    }

    @Override
    public List<RbacRoleMenu> queryByRoleId(int roleId) {
        Example example = new Example(RbacRoleMenu.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("roleId", roleId);
        return rbacRoleMenuMapper.selectByExample(example);
    }

    @Override
    @CacheEvict(cacheNames = LONG_TERM, key = "'DOMAIN:RBACROLEMENU:ID:' + #id")
    public void clearCacheById(int id) {
    }

}
