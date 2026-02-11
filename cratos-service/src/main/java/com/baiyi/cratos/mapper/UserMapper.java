package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.param.http.user.UserExtParam;
import com.baiyi.cratos.domain.param.http.user.UserParam;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface UserMapper extends Mapper<User> {

    List<User> queryPageByParam(UserParam.UserPageQueryParam param);

    List<User> queryExtUserPageByParam(UserExtParam.UserExtPageQueryParam param);

}