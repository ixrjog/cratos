package com.baiyi.cratos.service.channel.impl;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.domain.generator.Organization;
import com.baiyi.cratos.domain.param.http.channel.OrganizationParam;
import com.baiyi.cratos.mapper.OrganizationMapper;
import com.baiyi.cratos.service.channel.OrganizationService;
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
 * &#064;Date  2026/4/20 14:09
 * &#064;Version 1.0
 */
@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationMapper organizationMapper;

    @Override
    @CacheEvict(cacheNames = LONG_TERM, key = "'DOMAIN:ORGANIZATION:ID:' + #id")
    public void clearCacheById(int id) {
    }

    @Override
    public DataTable<Organization> queryOrganizationPage(OrganizationParam.OrganizationPageQuery pageQuery) {
        Page<Organization> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        Example example = new Example(EdsAssetIndex.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.hasText(pageQuery.getCode()
                                        .trim())) {
            criteria.andLike(
                    "code", SqlUtils.ofLike(pageQuery.getCode()
                                                    .trim())
            );
        }
        if (StringUtils.hasText(pageQuery.getQueryName()
                                        .trim())) {
            criteria.andLike(
                    "name", SqlUtils.ofLike(pageQuery.getQueryName()
                                                    .trim())
            );
        }
        List<Organization> data = organizationMapper.selectByExample(example);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public Organization getByUniqueKey(@NonNull Organization record) {
        Example example = new Example(Organization.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("code", record.getCode());
        return organizationMapper.selectOneByExample(example);
    }

}
