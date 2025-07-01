package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.generator.UserFavorite;
import com.baiyi.cratos.mapper.UserFavoriteMapper;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;
import lombok.NonNull;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/7/1 11:09
 * &#064;Version 1.0
 */
public interface UserFavoriteService extends BaseUniqueKeyService<UserFavorite, UserFavoriteMapper> {

    List<Integer> queryUserFavoriteBusinessIds(@NonNull String username, @NonNull String businessType);

}
