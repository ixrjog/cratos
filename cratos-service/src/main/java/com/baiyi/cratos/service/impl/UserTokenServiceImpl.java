package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.domain.generator.UserToken;
import com.baiyi.cratos.mapper.UserTokenMapper;
import com.baiyi.cratos.service.UserTokenService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

import static com.baiyi.cratos.common.configuration.CachingConfiguration.RepositoryName.LONG_TERM;

/**
 * @Author baiyi
 * @Date 2024/1/15 18:11
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
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
    public int countResourcesAuthorizedByToken(String token, String resource) {
        return userTokenMapper.countResourcesAuthorizedByToken(token, resource);
    }

    @Override
    public UserToken getByUniqueKey(@NonNull UserToken record) {
        return getByToken(record.getToken());
    }


    @Override
    @CacheEvict(cacheNames = LONG_TERM, key = "'DOMAIN:USERTOKEN:ID:' + #id")
    public void clearCacheById(int id) {
    }

}
