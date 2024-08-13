package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.annotation.DeleteBoundBusiness;
import com.baiyi.cratos.annotation.DomainDecrypt;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.param.user.UserParam;
import com.baiyi.cratos.mapper.UserMapper;
import com.baiyi.cratos.service.UserService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/1/10 10:19
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.USER)
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Override
    public DataTable<User> queryUserPage(UserParam.UserPageQuery pageQuery) {
        Page<User> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<User> data = userMapper.queryPageByParam(pageQuery);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    @DomainDecrypt
    public User getByUsername(String username) {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username", username);
        return userMapper.selectOneByExample(example);
    }

    @Override
    public User getByUniqueKey(@NonNull User record) {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username", record.getUsername());
        return userMapper.selectOneByExample(example);
    }

    @Override
    // 删除用户关联的业务标签、凭据
    @DeleteBoundBusiness(businessId = "#id", targetTypes = {BusinessTypeEnum.BUSINESS_TAG, BusinessTypeEnum.BUSINESS_CREDENTIAL})
    public void deleteById(int id) {
        userMapper.deleteByPrimaryKey(id);
    }

}
