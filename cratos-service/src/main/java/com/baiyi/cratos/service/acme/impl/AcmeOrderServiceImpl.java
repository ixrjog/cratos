package com.baiyi.cratos.service.acme.impl;

import com.baiyi.cratos.domain.generator.AcmeOrder;
import com.baiyi.cratos.mapper.AcmeOrderMapper;
import com.baiyi.cratos.service.acme.AcmeOrderService;
import com.baiyi.cratos.service.base.BaseService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.aop.framework.AopContext;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

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

    @Override
    public AcmeOrder getByOrderUrl(String orderUrl) {
        Example example = new Example(AcmeOrder.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderUrl", orderUrl);
        AcmeOrder acmeOrder = acmeOrderMapper.selectOneByExample(example);
        if (acmeOrder == null) {
            return null;
        }
        return (AcmeOrder) ((BaseService<?, ?>) AopContext.currentProxy()).getById(acmeOrder.getId());
    }

}
