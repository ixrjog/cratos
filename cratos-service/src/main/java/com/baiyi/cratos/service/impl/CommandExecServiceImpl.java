package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.mapper.CommandExecMapper;
import com.baiyi.cratos.service.CommandExecService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import static com.baiyi.cratos.common.configuration.CachingConfiguration.RepositoryName.LONG_TERM;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/13 16:04
 * &#064;Version 1.0
 */
@Service
@RequiredArgsConstructor
public class CommandExecServiceImpl implements CommandExecService {

    private final CommandExecMapper commandExecMapper;

    @Override
    @CacheEvict(cacheNames = LONG_TERM, key = "'DOMAIN:COMMANDEXEC:ID:' + #id")
    public void clearCacheById(int id) {
    }


}
