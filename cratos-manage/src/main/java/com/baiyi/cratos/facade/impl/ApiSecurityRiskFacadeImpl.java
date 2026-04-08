package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.common.util.PasswordGenerator;
import com.baiyi.cratos.common.util.SessionUtils;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.ApiSecurityRisk;
import com.baiyi.cratos.domain.param.http.security.ApiSecurityRiskParam;
import com.baiyi.cratos.domain.view.security.ApiSecurityRiskVO;
import com.baiyi.cratos.facade.ApiSecurityRiskFacade;
import com.baiyi.cratos.service.security.ApiSecurityRiskService;
import com.baiyi.cratos.wrapper.security.ApiSecurityRiskWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/4/7 10:09
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
public class ApiSecurityRiskFacadeImpl implements ApiSecurityRiskFacade {

    private final ApiSecurityRiskService apiSecurityRiskService;
    private final ApiSecurityRiskWrapper apiSecurityRiskWrapper;

    @Override
    public DataTable<ApiSecurityRiskVO.Risk> queryRiskPage(ApiSecurityRiskParam.RiskPageQuery pageQuery) {
        if (StringUtils.hasText(pageQuery.getRiskNo())) {
            pageQuery.setQueryName(null);
        }
        DataTable<ApiSecurityRisk> table = apiSecurityRiskService.queryApiSecurityRiskPage(pageQuery);
        return apiSecurityRiskWrapper.wrapToTarget(table);
    }

    @Override
    public void addRisk(ApiSecurityRiskParam.AddRisk addRisk) {
        ApiSecurityRisk risk = addRisk.toTarget();
        risk.setRiskNo(PasswordGenerator.generateNo());
        risk.setValid(true);
        risk.setCompleted(false);
        if (addRisk.getDiscoveredTime() == null) {
            risk.setDiscoveredTime(new java.util.Date());
        }
        if (StringUtils.hasText(addRisk.getSecurityOfficer())) {
            risk.setSecurityOfficer(SessionUtils.getUsername());
        }
        apiSecurityRiskService.add(risk);
    }

    @Override
    public void updateRisk(ApiSecurityRiskParam.UpdateRisk updateRisk) {
        apiSecurityRiskService.updateByPrimaryKey(updateRisk.toTarget());
    }

    @Override
    public void deleteRiskById(int id) {
        apiSecurityRiskService.deleteById(id);
    }

}
