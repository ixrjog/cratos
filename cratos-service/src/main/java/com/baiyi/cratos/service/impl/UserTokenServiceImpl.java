package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.UserToken;
import com.baiyi.cratos.mapper.UserTokenMapper;
import com.baiyi.cratos.service.UserTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

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
        Example example = new Example(UserToken.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("token", token);
        return userTokenMapper.selectOneByExample(example);
    }

    @Override
    public List<UserToken> queryValidTokenByUsername(String username) {
        Example example = new Example(UserToken.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username", username)
                .andEqualTo("valid", true);
        return userTokenMapper.selectByExample(example);
    }

    @Override
    public UserTokenMapper getMapper() {
        return userTokenMapper;
    }

    @Override
    public UserToken getByUniqueKey(UserToken userToken) {
        return getByToken(userToken.getToken());
    }

}
