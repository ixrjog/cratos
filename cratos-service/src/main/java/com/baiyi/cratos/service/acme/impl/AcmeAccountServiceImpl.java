package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.domain.generator.AcmeAccount;
import com.baiyi.cratos.mapper.AcmeAccountMapper;
import com.baiyi.cratos.service.AcmeAccountService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import static com.baiyi.cratos.common.configuration.CachingConfiguration.RepositoryName.LONG_TERM;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/2/9 18:10
 * &#064;Version 1.0
 */
@Service
@RequiredArgsConstructor
public class AcmeAccountServiceImpl implements AcmeAccountService {

    private final AcmeAccountMapper acmeAccountMapper;

    @Override
    @CacheEvict(cacheNames = LONG_TERM, key = "'ACMEACCOUNT:ID:' + #id")
    public void clearCacheById(int id) {
    }

    @Override
    public AcmeAccount getByUniqueKey(@NonNull AcmeAccount record) {
        Example example = new Example(AcmeAccount.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("email", record.getEmail())
                .andEqualTo("acmeProvider", record.getAcmeProvider());
        return acmeAccountMapper.selectOneByExample(example);
    }

}
