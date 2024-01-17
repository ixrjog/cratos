package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.domain.generator.RbacResource;
import com.baiyi.cratos.mapper.RbacResourceMapper;
import com.baiyi.cratos.service.RbacResourceService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

/**
 * @Author baiyi
 * @Date 2024/1/17 11:07
 * @Version 1.0
 */
@Service
@AllArgsConstructor
public class RbacResourceServiceImpl implements RbacResourceService {

    private final RbacResourceMapper rbacResourceMapper;

    @Override
    public RbacResourceMapper getMapper() {
        return rbacResourceMapper;
    }

    @Override
    public RbacResource getByUniqueKey(RbacResource rbacResource) {
        Example example = new Example(RbacResource.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("resourceName", rbacResource.getResourceName());
        return rbacResourceMapper.selectOneByExample(example);
    }

}
