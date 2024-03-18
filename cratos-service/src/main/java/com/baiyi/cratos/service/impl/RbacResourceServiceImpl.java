package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.RbacResource;
import com.baiyi.cratos.domain.param.rbac.RbacResourceParam;
import com.baiyi.cratos.domain.param.rbac.RbacRoleResourceParam;
import com.baiyi.cratos.mapper.RbacResourceMapper;
import com.baiyi.cratos.service.RbacResourceService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/1/17 11:07
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class RbacResourceServiceImpl implements RbacResourceService {

    private final RbacResourceMapper rbacResourceMapper;

    @Override
    public RbacResource getByUniqueKey(RbacResource rbacResource) {
        Example example = new Example(RbacResource.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("resourceName", rbacResource.getResourceName());
        return rbacResourceMapper.selectOneByExample(example);
    }

    @Override
    public int selectCountByGroupId(int groupId) {
        Example example = new Example(RbacResource.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("groupId", groupId);
        return rbacResourceMapper.selectCountByExample(example);
    }

    @Override
    public DataTable<RbacResource> queryPageByParam(RbacResourceParam.ResourcePageQuery pageQuery) {
        Page<RbacResource> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<RbacResource> data = rbacResourceMapper.queryPageByParam(pageQuery);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public DataTable<RbacResource> queryRoleResourcePageByParam(RbacRoleResourceParam.RoleResourcePageQuery pageQuery) {
        Page<RbacResource> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<RbacResource> data = pageQuery.getInRole() ? rbacResourceMapper.queryResourceInRoleByParam(pageQuery)
                : rbacResourceMapper.queryResourceNotInRoleByParam(pageQuery);
        return new DataTable<>(data, page.getTotal());
    }

}
