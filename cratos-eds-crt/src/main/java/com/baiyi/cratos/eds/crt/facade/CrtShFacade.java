package com.baiyi.cratos.eds.crt.facade;

import com.baiyi.cratos.eds.crt.model.CrtSh;
import com.baiyi.cratos.eds.crt.repo.CrtShRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/1/16 10:01
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CrtShFacade {

    private final CrtShRepo crtShRepo;

    @Retryable(maxAttempts = 2, backoff = @Backoff(delay = 5000))
    public List<CrtSh.CertificateLog> queryCertificateLogs(String domain) {
        log.info("crt.sh certificate search: {}", domain);
        List<CrtSh.CertificateLog> certificateLogs = crtShRepo.queryCertificateLogs(domain);
        if (!CollectionUtils.isEmpty(certificateLogs)) {
            // 设置归属域名
            certificateLogs.forEach(e -> e.setDomainName(domain));
        }
        return certificateLogs;
    }

}
