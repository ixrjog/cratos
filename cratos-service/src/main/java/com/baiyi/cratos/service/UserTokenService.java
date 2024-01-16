package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.generator.UserToken;
import com.baiyi.cratos.mapper.UserTokenMapper;
import com.baiyi.cratos.service.base.BaseService;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/1/15 18:11
 * @Version 1.0
 */
public interface UserTokenService extends BaseUniqueKeyService<UserToken>, BaseService<UserToken, UserTokenMapper> {

    UserToken getByToken(String token);

    List<UserToken> queryValidTokenByUsername(String username);

}
