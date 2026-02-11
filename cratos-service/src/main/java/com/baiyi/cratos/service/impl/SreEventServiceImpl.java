package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.domain.generator.SreEvent;
import com.baiyi.cratos.mapper.SreEventMapper;
import com.baiyi.cratos.service.SreEventService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import static com.baiyi.cratos.common.configuration.CachingConfiguration.RepositoryName.LONG_TERM;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/2/9 11:50
 * &#064;Version 1.0
 */
@Service
@RequiredArgsConstructor
public class SreEventServiceImpl implements SreEventService {

    private final SreEventMapper sreEventMapper;

    @Override
    @CacheEvict(cacheNames = LONG_TERM, key = "'SREEVENT:ID:' + #id")
    public void clearCacheById(int id) {
    }

    @Override
    public SreEvent getByUniqueKey(@NonNull SreEvent record) {
        return null;
    }
}
