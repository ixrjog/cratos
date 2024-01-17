package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.UserToken;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface UserTokenMapper extends Mapper<UserToken> {

    int countResourcesAuthorizedByToken(@Param("token") String token, @Param("resource") String resource);

}