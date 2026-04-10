package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.ApiSecurityRisk;
import com.baiyi.cratos.domain.param.http.security.ApiSecurityRiskParam;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface ApiSecurityRiskMapper extends Mapper<ApiSecurityRisk> {

    List<ApiSecurityRisk> queryPageByParam(ApiSecurityRiskParam.RiskPageQuery pageQuery);

    List<ApiSecurityRisk> selectAll();

}