package com.baiyi.cratos.service.acme.impl;

import com.baiyi.cratos.domain.generator.AcmeCertificateDeployment;
import com.baiyi.cratos.mapper.AcmeCertificateDeploymentMapper;
import com.baiyi.cratos.service.acme.AcmeCertificateDeploymentService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import static com.baiyi.cratos.common.configuration.CachingConfiguration.RepositoryName.LONG_TERM;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/3/3 16:26
 * &#064;Version 1.0
 */
@Service
@RequiredArgsConstructor
public class AcmeCertificateDeploymentServiceImpl implements AcmeCertificateDeploymentService {

    private final AcmeCertificateDeploymentMapper acmeCertificateDeploymentMapper;

    @Override
    @CacheEvict(cacheNames = LONG_TERM, key = "'ACMECERTDEPLOYMENT:ID:' + #id")
    public void clearCacheById(int id) {
    }

    @Override
    public AcmeCertificateDeployment getByUniqueKey(@NonNull AcmeCertificateDeployment record) {
        Example example = new Example(AcmeCertificateDeployment.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("edsInstanceId", record.getEdsInstanceId())
                .andEqualTo("certificateId", record.getCertificateId());
        return acmeCertificateDeploymentMapper.selectOneByExample(example);
    }

}
