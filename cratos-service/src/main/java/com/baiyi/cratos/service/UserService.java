package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.param.user.UserParam;
import com.baiyi.cratos.mapper.UserMapper;
import com.baiyi.cratos.service.base.BaseService;

/**
 * @Author baiyi
 * @Date 2024/1/10 10:19
 * @Version 1.0
 */
public interface UserService extends BaseService<User, UserMapper> {

    DataTable<User> queryUserPage(UserParam.UserPageQuery pageQuery);

    User getByUsername(String username);

}