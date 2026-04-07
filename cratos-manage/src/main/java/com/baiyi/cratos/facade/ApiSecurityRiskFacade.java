package com.baiyi.cratos.facade;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.http.security.ApiSecurityRiskParam;
import com.baiyi.cratos.domain.view.security.ApiSecurityRiskVO;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/4/7 10:09
 * &#064;Version 1.0
 */
public interface ApiSecurityRiskFacade {

    DataTable<ApiSecurityRiskVO.Risk> queryRiskPage(ApiSecurityRiskParam.RiskPageQuery pageQuery);

    void addRisk(ApiSecurityRiskParam.AddRisk addRisk);

    void updateRisk(ApiSecurityRiskParam.UpdateRisk updateRisk);

    void deleteRiskById(int id);

}
