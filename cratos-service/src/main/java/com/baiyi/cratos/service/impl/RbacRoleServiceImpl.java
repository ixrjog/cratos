package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.RbacRole;
import com.baiyi.cratos.domain.param.http.rbac.RbacRoleParam;
import com.baiyi.cratos.mapper.RbacRoleMapper;
import com.baiyi.cratos.service.RbacRoleService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.baiyi.cratos.common.configuration.CachingConfiguration.RepositoryName.LONG_TERM;

/**
 * @Author baiyi
 * @Date 2024/1/17 17:53
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class RbacRoleServiceImpl implements RbacRoleService {

    private final RbacRoleMapper rbacRoleMapper;

    @Override
    public DataTable<RbacRole> queryPageByParam(RbacRoleParam.RolePageQuery pageQuery) {
        Page<RbacRole> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<RbacRole> data = rbacRoleMapper.queryPageByParam(pageQuery);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    @CacheEvict(cacheNames = LONG_TERM, key = "'DOMAIN:RBACROLE:ID:' + #id")
    public void clearCacheById(int id) {
    }

}
