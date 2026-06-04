package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.mapper.KubernetesDeploymentAppVersionComparisonMapper;
import com.baiyi.cratos.service.KubernetesDeploymentAppVersionComparisonService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import static com.baiyi.cratos.common.configuration.CachingConfiguration.RepositoryName.LONG_TERM;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/6/3 17:03
 * &#064;Version 1.0
 */
@Service
@RequiredArgsConstructor
public class KubernetesDeploymentAppVersionComparisonServiceImpl implements KubernetesDeploymentAppVersionComparisonService {

    private final KubernetesDeploymentAppVersionComparisonMapper kubernetesDeploymentAppVersionComparisonMapper;

    @Override
    @CacheEvict(cacheNames = LONG_TERM, key = "'DOMAIN:KUBERNETES_DEPLOYMENT_APP_VERSION_COMPARISON:ID:' + #id")
    public void clearCacheById(int id) {
    }

}
