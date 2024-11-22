package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.param.http.user.UserParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface UserMapper extends Mapper<User> {

    List<User> queryPageByParam(UserParam.UserPageQuery pageQuery);

}