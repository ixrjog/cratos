package com.baiyi.cratos.service.security.impl;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.ApiSecurityRisk;
import com.baiyi.cratos.domain.param.http.security.ApiSecurityRiskParam;
import com.baiyi.cratos.mapper.ApiSecurityRiskMapper;
import com.baiyi.cratos.service.security.ApiSecurityRiskService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.baiyi.cratos.common.configuration.CachingConfiguration.RepositoryName.LONG_TERM;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/4/3 16:37
 * &#064;Version 1.0
 */
@Service
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.API_SECURITY_RISK)
public class ApiSecurityRiskServiceImpl implements ApiSecurityRiskService {

    private final ApiSecurityRiskMapper apiSecurityRiskMapper;

    @Override
    @CacheEvict(cacheNames = LONG_TERM, key = "'API:SECURITY:RISK:ID:' + #id")
    public void clearCacheById(int id) {
    }

    @Override
    public DataTable<ApiSecurityRisk> queryApiSecurityRiskPage(ApiSecurityRiskParam.RiskPageQuery pageQuery) {
        Page<ApiSecurityRisk> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<ApiSecurityRisk> data = apiSecurityRiskMapper.queryPageByParam(pageQuery);
        return new DataTable<>(data, page.getTotal(), pageQuery);
    }

}
