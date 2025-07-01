package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.domain.generator.UserFavorite;
import com.baiyi.cratos.mapper.UserFavoriteMapper;
import com.baiyi.cratos.service.UserFavoriteService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

import static com.baiyi.cratos.common.configuration.CachingConfiguration.RepositoryName.LONG_TERM;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/7/1 11:09
 * &#064;Version 1.0
 */
@Service
@RequiredArgsConstructor
public class UserFavoriteServiceImpl implements UserFavoriteService {

    private final UserFavoriteMapper userFavoriteMapper;

    @Override
    @CacheEvict(cacheNames = LONG_TERM, key = "'DOMAIN:USERFAVORITE:ID:' + #id")
    public void clearCacheById(int id) {
    }

    @Override
    public UserFavorite getByUniqueKey(@NonNull UserFavorite record) {
        Example example = new Example(UserFavorite.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username", record.getUsername())
                .andEqualTo("businessType", record.getBusinessType())
                .andEqualTo("businessId", record.getBusinessId());
        return userFavoriteMapper.selectOneByExample(example);
    }

    @Override
    public List<Integer> queryUserFavoriteBusinessIds(@NonNull String username, @NonNull String businessType) {
        Example example = new Example(UserFavorite.class);
        example.createCriteria()
                .andEqualTo("username", username)
                .andEqualTo("businessType", businessType);
        example.setOrderByClause("seq DESC");
        return userFavoriteMapper.selectByExample(example)
                .stream()
                .map(UserFavorite::getBusinessId)
                .toList();
    }

}
