package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.generator.UserPermission;
import com.baiyi.cratos.mapper.UserPermissionMapper;
import com.baiyi.cratos.service.UserPermissionService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import static com.baiyi.cratos.common.configuration.CachingConfiguration.RepositoryName.LONG_TERM;

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
    public UserPermission getByUniqueKey(@NonNull UserPermission record) {
        Example example = new Example(UserPermission.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("businessType", record.getBusinessType())
                .andEqualTo("businessId", record.getBusinessId())
                .andEqualTo("username", record.getUsername())
                .andEqualTo("role", record.getRole());
        return userPermissionMapper.selectOneByExample(example);
    }

    @Override
    @CacheEvict(cacheNames = LONG_TERM, key = "'DOMAIN:USERPERMISSION:ID:' + #id")
    public void clearCacheById(int id) {
    }

    @Override
    public boolean contains(String username, BaseBusiness.HasBusiness hasBusiness) {
        Example example = new Example(UserPermission.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("businessType", hasBusiness.getBusinessType())
                .andEqualTo("businessId", hasBusiness.getBusinessId())
                .andEqualTo("username", username);
        return userPermissionMapper.selectCountByExample(example) != 0;
    }

    @Override
    public boolean contains(String username, BaseBusiness.HasBusiness hasBusiness, String role) {
        UserPermission uniqueKey = UserPermission.builder()
                .username(username)
                .businessType(hasBusiness.getBusinessType())
                .businessId(hasBusiness.getBusinessId())
                .role(role)
                .build();
        return getByUniqueKey(uniqueKey) != null;
    }

}
