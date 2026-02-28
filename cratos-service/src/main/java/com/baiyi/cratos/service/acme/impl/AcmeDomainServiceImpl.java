package com.baiyi.cratos.service.acme.impl;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.AcmeDomain;
import com.baiyi.cratos.domain.param.http.acme.AcmeDomainParam;
import com.baiyi.cratos.mapper.AcmeDomainMapper;
import com.baiyi.cratos.service.acme.AcmeDomainService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

import static com.baiyi.cratos.common.configuration.CachingConfiguration.RepositoryName.LONG_TERM;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/2/10 17:08
 * &#064;Version 1.0
 */
@Service
@RequiredArgsConstructor
public class AcmeDomainServiceImpl implements AcmeDomainService {

    private final AcmeDomainMapper acmeDomainMapper;

    @Override
    @CacheEvict(cacheNames = LONG_TERM, key = "'ACMEDOMAIN:ID:' + #id")
    public void clearCacheById(int id) {
    }

    @Override
    public AcmeDomain getByUniqueKey(@NonNull AcmeDomain record) {
        Example example = new Example(AcmeDomain.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("domains", record.getDomains());
        return acmeDomainMapper.selectOneByExample(example);
    }

    @Override
    public DataTable<AcmeDomain> queryAcmeDomainPage(AcmeDomainParam.DomainPageQuery pageQuery) {
        Page<AcmeDomain> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<AcmeDomain> data = acmeDomainMapper.queryPageByParam(pageQuery);
        return new DataTable<>(data, page.getTotal(), pageQuery);
    }

}
