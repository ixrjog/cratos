package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.RbacGroup;
import com.baiyi.cratos.domain.param.http.rbac.RbacGroupParam;
import com.baiyi.cratos.mapper.RbacGroupMapper;
import com.baiyi.cratos.service.RbacGroupService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

import static com.baiyi.cratos.common.configuration.CachingConfiguration.RepositoryName.LONG_TERM;

/**
 * @Author baiyi
 * @Date 2024/1/24 15:03
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class RbacGroupServiceImpl implements RbacGroupService {

    private final RbacGroupMapper rbacGroupMapper;

    @Override
    public DataTable<RbacGroup> queryPageByParam(RbacGroupParam.GroupPageQuery pageQuery) {
        Page<RbacGroup> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<RbacGroup> data = rbacGroupMapper.queryPageByParam(pageQuery);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public RbacGroup getByUniqueKey(@NonNull RbacGroup record) {
        Example example = new Example(RbacGroup.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("groupName", record.getGroupName());
        return rbacGroupMapper.selectOneByExample(example);
    }

    @Override
    @CacheEvict(cacheNames = LONG_TERM, key = "'DOMAIN:RBACGROUP:ID:' + #id")
    public void clearCacheById(int id) {
    }

}
