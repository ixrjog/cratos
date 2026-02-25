package com.baiyi.cratos.service.acme.impl;

import com.baiyi.cratos.domain.generator.AcmeCertificate;
import com.baiyi.cratos.mapper.AcmeCertificateMapper;
import com.baiyi.cratos.service.acme.AcmeCertificateService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import static com.baiyi.cratos.common.configuration.CachingConfiguration.RepositoryName.LONG_TERM;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/2/13 11:36
 * &#064;Version 1.0
 */
@Service
@RequiredArgsConstructor
public class AcmeCertificateServiceImpl implements AcmeCertificateService {

    private final AcmeCertificateMapper acmeCertificateMapper;

    @Override
    @CacheEvict(cacheNames = LONG_TERM, key = "'ACMECERT:ID:' + #id")
    public void clearCacheById(int id) {
    }

    @Override
    public AcmeCertificate getByUniqueKey(@NonNull AcmeCertificate record) {
        return null;
    }

}
