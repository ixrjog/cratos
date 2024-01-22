package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.domain.generator.UserPermission;
import com.baiyi.cratos.mapper.UserPermissionMapper;
import com.baiyi.cratos.service.UserPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

/**
 * @Author baiyi
 * @Date 2024/1/18 17:33
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class UserPermissionServiceImpl implements UserPermissionService {

    private final UserPermissionMapper userPermissionMapper;

    @Override
    public UserPermission getByUniqueKey(UserPermission userPermission) {
        Example example = new Example(UserPermission.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("businessType", userPermission.getBusinessType())
                .andEqualTo("businessId", userPermission.getBusinessId())
                .andEqualTo("username",userPermission.getUsername());
        return userPermissionMapper.selectOneByExample(example);
    }

}
