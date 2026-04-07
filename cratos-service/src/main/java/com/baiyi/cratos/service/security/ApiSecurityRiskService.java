package com.baiyi.cratos.service.security;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.ApiSecurityRisk;
import com.baiyi.cratos.domain.param.http.security.ApiSecurityRiskParam;
import com.baiyi.cratos.mapper.ApiSecurityRiskMapper;
import com.baiyi.cratos.service.base.BaseValidService;
import com.baiyi.cratos.service.base.SupportBusinessService;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/4/3 16:36
 * &#064;Version 1.0
 */
public interface ApiSecurityRiskService extends BaseValidService<ApiSecurityRisk, ApiSecurityRiskMapper>, SupportBusinessService {

    DataTable<ApiSecurityRisk> queryApiSecurityRiskPage(ApiSecurityRiskParam.RiskPageQuery pageQuery);

}
