package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.RbacGroup;
import com.baiyi.cratos.domain.param.rbac.RbacGroupParam;
import com.baiyi.cratos.mapper.RbacGroupMapper;
import com.baiyi.cratos.service.RbacGroupService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/1/24 15:03
 * @Version 1.0
 */
@Service
@AllArgsConstructor
public class RbacGroupServiceImpl implements RbacGroupService {

    private final RbacGroupMapper rbacGroupMapper;

    @Override
    public DataTable<RbacGroup> queryPageByParam(RbacGroupParam.GroupPageQuery pageQuery) {
        Page<RbacGroup> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<RbacGroup> data = rbacGroupMapper.queryPageByParam(pageQuery);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public RbacGroup getByUniqueKey(RbacGroup rbacGroup) {
        Example example = new Example(RbacGroup.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("groupName", rbacGroup.getGroupName());
        return rbacGroupMapper.selectOneByExample(example);
    }

}
