package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.domain.generator.RbacUserRole;
import com.baiyi.cratos.mapper.RbacUserRoleMapper;
import com.baiyi.cratos.service.RbacUserRoleService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Objects;

import static com.baiyi.cratos.common.configuration.CachingConfiguration.RepositoryName.LONG_TERM;

/**
 * @Author baiyi
 * @Date 2024/1/22 17:26
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class RbacUserRoleServiceImpl implements RbacUserRoleService {

    private final RbacUserRoleMapper rbacUserRoleMapper;

    @Override
    public List<RbacUserRole> queryByUsername(String username) {
        Example example = new Example(RbacUserRole.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username", username);
        return rbacUserRoleMapper.selectByExample(example);
    }

    @Override
    public int selectCountByUsername(String username) {
        Example example = new Example(RbacUserRole.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username", username);
        return rbacUserRoleMapper.selectCountByExample(example);
    }

    @Override
    public int selectCountByRoleId(int roleId) {
        Example example = new Example(RbacUserRole.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("roleId", roleId);
        return rbacUserRoleMapper.selectCountByExample(example);
    }

    @Override
    public RbacUserRole getByUniqueKey(@NonNull RbacUserRole record) {
        Example example = new Example(RbacUserRole.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username", record.getUsername())
                .andEqualTo("roleId", record.getRoleId());
        return rbacUserRoleMapper.selectOneByExample(example);
    }

    @Override
    public int queryUserMaxAccessLevel(String username) {
        Integer maxAccessLevel = rbacUserRoleMapper.queryUserMaxAccessLevel(username);
        return Objects.requireNonNullElse(maxAccessLevel, 0);
    }

    @Override
    @CacheEvict(cacheNames = LONG_TERM, key = "'DOMAIN:RBACUSERROLE:ID:' + #id")
    public void clearCacheById(int id) {
    }

}
