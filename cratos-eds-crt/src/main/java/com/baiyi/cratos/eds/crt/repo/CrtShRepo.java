package com.baiyi.cratos.eds.crt.repo;

import com.baiyi.cratos.common.configuration.CachingConfiguration;
import com.baiyi.cratos.eds.crt.model.CrtSh;
import com.baiyi.cratos.eds.crt.service.CrtShService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/1/14 18:13
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CrtShRepo {

    private final CrtShService crtShService;

    @Cacheable(cacheNames = CachingConfiguration.RepositoryName.SHORT_TERM, key = "'CRTSH:CERTIFICATELOG:DOMAIN:' + #domain", unless = "#result == null")
    public List<CrtSh.CertificateLog> queryCertificateLogs(String domain) {
        return crtShService.queryCertificateLogs(domain);
    }

}
