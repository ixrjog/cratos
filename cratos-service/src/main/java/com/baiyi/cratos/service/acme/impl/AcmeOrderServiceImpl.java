package com.baiyi.cratos.service.acme.impl;

import com.baiyi.cratos.domain.generator.AcmeOrder;
import com.baiyi.cratos.mapper.AcmeOrderMapper;
import com.baiyi.cratos.service.acme.AcmeOrderService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import static com.baiyi.cratos.common.configuration.CachingConfiguration.RepositoryName.LONG_TERM;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/2/10 17:39
 * &#064;Version 1.0
 */
@Service
@RequiredArgsConstructor
public class AcmeOrderServiceImpl implements AcmeOrderService {

    private final AcmeOrderMapper acmeOrderMapper;

    @Override
    @CacheEvict(cacheNames = LONG_TERM, key = "'ACMEORDER:ID:' + #id")
    public void clearCacheById(int id) {

    }

    @Override
    public AcmeOrder getByUniqueKey(@NonNull AcmeOrder record) {
        return null;
    }

}
