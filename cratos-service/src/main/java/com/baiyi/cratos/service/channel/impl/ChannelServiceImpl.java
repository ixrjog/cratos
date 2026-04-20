package com.baiyi.cratos.service.channel.impl;

import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.service.channel.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import static com.baiyi.cratos.common.configuration.CachingConfiguration.RepositoryName.LONG_TERM;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/4/20 17:09
 * &#064;Version 1.0
 */
@Service
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.CHANNEL)
public class ChannelServiceImpl implements ChannelService {

    @Override
    @CacheEvict(cacheNames = LONG_TERM, key = "'DOMAIN:CHANNEL:ID:' + #id")
    public void clearCacheById(int id) {
    }

}
