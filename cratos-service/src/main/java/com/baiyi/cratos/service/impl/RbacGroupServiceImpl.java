package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.domain.generator.RbacGroup;
import com.baiyi.cratos.mapper.RbacGroupMapper;
import com.baiyi.cratos.service.RbacGroupService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

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
    public RbacGroup getByUniqueKey(RbacGroup rbacGroup) {
        Example example = new Example(RbacGroup.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("groupName", rbacGroup.getGroupName());
        return rbacGroupMapper.selectOneByExample(example);
    }

}
