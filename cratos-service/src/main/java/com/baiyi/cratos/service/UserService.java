package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.param.http.user.UserExtParam;
import com.baiyi.cratos.domain.param.http.user.UserParam;
import com.baiyi.cratos.mapper.UserMapper;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;
import com.baiyi.cratos.service.base.BaseValidService;
import com.baiyi.cratos.service.base.SupportBusinessService;

/**
 * @Author baiyi
 * @Date 2024/1/10 10:19
 * @Version 1.0
 */
public interface UserService extends BaseUniqueKeyService<User, UserMapper>, BaseValidService<User, UserMapper>, SupportBusinessService {

    DataTable<User> queryUserPage(UserParam.UserPageQueryParam param);

    DataTable<User> queryExtUserPage(UserExtParam.UserExtPageQueryParam param);

    User getByUsername(String username);

}