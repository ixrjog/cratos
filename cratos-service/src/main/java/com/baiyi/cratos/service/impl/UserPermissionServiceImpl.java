package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.UserPermission;
import com.baiyi.cratos.domain.param.http.user.UserPermissionParam;
import com.baiyi.cratos.mapper.UserPermissionMapper;
import com.baiyi.cratos.service.UserPermissionService;
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
 * @Date 2024/1/18 17:33
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class UserPermissionServiceImpl implements UserPermissionService {

    private final UserPermissionMapper userPermissionMapper;

    @Override
    public DataTable<UserPermission> queryUserPermissionPage(UserPermissionParam.UserPermissionPageQuery pageQuery) {
        Page<UserPermission> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<UserPermission> data = userPermissionMapper.queryPageByParam(pageQuery);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public UserPermission getByUniqueKey(@NonNull UserPermission record) {
        Example example = new Example(UserPermission.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("businessType", record.getBusinessType())
                .andEqualTo("businessId", record.getBusinessId())
                .andEqualTo("username", record.getUsername())
                .andEqualTo("role", record.getRole())
                .andEqualTo("name", record.getName());
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
    public List<UserPermission> queryByUsername(String username) {
        Example example = new Example(UserPermission.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username", username);
        return userPermissionMapper.selectByExample(example);
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

    @Override
    public boolean containsTagGroup(String username, String group, String role) {
        UserPermission uniqueKey = UserPermission.builder()
                .username(username)
                .businessType(BusinessTypeEnum.TAG_GROUP.name())
                .businessId(group.hashCode())
                .role(role)
                .build();
        return getByUniqueKey(uniqueKey) != null;
    }

    @Override
    public UserPermission getUserPermissionTagGroup(String username, String group, String role) {
        UserPermission uniqueKey = UserPermission.builder()
                .username(username)
                .businessType(BusinessTypeEnum.TAG_GROUP.name())
                .businessId(group.hashCode())
                .role(role)
                .build();
        return getByUniqueKey(uniqueKey);
    }

    @Override
    public List<UserPermission> queryByBusiness(BaseBusiness.HasBusiness hasBusiness) {
        Example example = new Example(UserPermission.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("businessType", hasBusiness.getBusinessType())
                .andEqualTo("businessId", hasBusiness.getBusinessId());
        return userPermissionMapper.selectByExample(example);
    }

    @Override
    public List<Integer> queryUserPermissionBusinessIds(String username, String businessType) {
        return userPermissionMapper.queryUserPermissionBusinessIds(username, businessType);
    }

    @Override
    public List<String> queryUserPermissionUsernames(String businessType, int businessId) {
        return userPermissionMapper.queryUserPermissionUsernames(businessType, businessId);
    }

    @Override
    public List<UserPermission> queryUserPermissionByBusiness(String username, BaseBusiness.HasBusiness hasBusiness) {
        Example example = new Example(UserPermission.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username", username)
                .andEqualTo("businessType", hasBusiness.getBusinessType())
                .andEqualTo("businessId", hasBusiness.getBusinessId());
        return userPermissionMapper.selectByExample(example);
    }

    /**
     * 查询用户授权的Group
     *
     * @param username
     * @return
     */
    @Override
    public List<String> queryUserPermissionGroups(String username) {
        return userPermissionMapper.queryUserPermissionGroups(username);
    }

}
