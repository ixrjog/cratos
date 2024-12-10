package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.domain.generator.BusinessCommit;
import com.baiyi.cratos.mapper.BusinessCommitMapper;
import com.baiyi.cratos.service.BusinessCommitService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import static com.baiyi.cratos.common.configuration.CachingConfiguration.RepositoryName.LONG_TERM;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/10 14:04
 * &#064;Version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BusinessCommitServiceImpl implements BusinessCommitService {

    private final BusinessCommitMapper businessCommitMapper;

    @Override
    @CacheEvict(cacheNames = LONG_TERM, key = "'DOMAIN:BUSINESSCOMMIT:ID:' + #id")
    public void clearCacheById(int id) {
    }

    @Override
    public BusinessCommit getByUniqueKey(@NonNull BusinessCommit record) {
        Example example = new Example(BusinessCommit.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("commitId", record.getCommitId());
        return businessCommitMapper.selectOneByExample(example);
    }

}
