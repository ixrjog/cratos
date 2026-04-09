package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.AccountEntity;
import com.baiyi.cratos.domain.generator.ApplicationResourceBaselineMember;
import com.baiyi.cratos.domain.param.http.account.AccountEntityParam;
import com.baiyi.cratos.mapper.AccountEntityMapper;
import com.baiyi.cratos.service.AccountEntityService;
import com.baiyi.cratos.util.SqlUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

import static com.baiyi.cratos.common.configuration.CachingConfiguration.RepositoryName.LONG_TERM;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/4/8 13:25
 * &#064;Version 1.0
 */
@Service
@RequiredArgsConstructor
public class AccountEntityServiceImpl implements AccountEntityService {

    private final AccountEntityMapper accountEntityMapper;

    @Override
    public DataTable<AccountEntity> queryAccountEntityPage(AccountEntityParam.AccountEntityPageQuery pageQuery) {
        Page<AccountEntity> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        Example example = new Example(AccountEntity.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.hasText(pageQuery.getQueryName())) {
            criteria.andLike("name", SqlUtils.ofLike(pageQuery.getQueryName()));
        }
        List<AccountEntity> data = accountEntityMapper.selectByExample(example);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    @CacheEvict(cacheNames = LONG_TERM, key = "'ACCOUNTENTITY:ID:' + #id")
    public void clearCacheById(int id) {
    }

    @Override
    public AccountEntity getByUniqueKey(@NonNull AccountEntity record) {
        Example example = new Example(ApplicationResourceBaselineMember.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("name", record.getName());
        return accountEntityMapper.selectOneByExample(example);
    }

}
