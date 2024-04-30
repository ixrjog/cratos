package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.domain.generator.RbacRoleResource;
import com.baiyi.cratos.mapper.RbacRoleResourceMapper;
import com.baiyi.cratos.service.RbacRoleResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/3/15 10:18
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class RbacRoleResourceServiceImpl implements RbacRoleResourceService {

    private final RbacRoleResourceMapper rbacRoleResourceMapper;

    @Override
    public int selectCountByRoleId(int roleId) {
        Example example = new Example(RbacRoleResource.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("roleId", roleId);
        return rbacRoleResourceMapper.selectCountByExample(example);
    }

    @Override
    public RbacRoleResource getByUniqueKey(RbacRoleResource record) {
        Example example = new Example(RbacRoleResource.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("roleId", record.getRoleId())
                .andEqualTo("resourceId", record.getResourceId());
        return rbacRoleResourceMapper.selectOneByExample(example);
    }

    @Override
    public List<RbacRoleResource> queryByResourceId(int resourceId) {
        Example example = new Example(RbacRoleResource.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("resourceId", resourceId);
        return rbacRoleResourceMapper.selectByExample(example);
    }

}
