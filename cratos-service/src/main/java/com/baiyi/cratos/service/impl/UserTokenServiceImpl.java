package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.UserToken;
import com.baiyi.cratos.mapper.UserTokenMapper;
import com.baiyi.cratos.service.UserTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @Author baiyi
 * @Date 2024/1/15 18:11
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.USER)
public class UserTokenServiceImpl implements UserTokenService {

    private final UserTokenMapper userTokenMapper;

    @Override
    public UserToken getByToken(String token) {
       // return userTokenMapper.selectByPrimaryKey();
        return null;
    }

    @Override
    public UserTokenMapper getMapper() {
        return null;
    }

    @Override
    public UserToken getByUniqueKey(UserToken userToken) {
        return null;
    }
}
