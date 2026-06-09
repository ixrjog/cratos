package com.baiyi.cratos.facade.security;

import com.baiyi.cratos.domain.generator.ApiScanResult;
import com.baiyi.cratos.domain.view.security.ApiScanVO;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/6/5 11:29
 * &#064;Version 1.0
 */
public interface ApiScanFacade {

    void scan(ApiScanVO.ScanConfig scanConfig);

    void scan(ApiScanVO.ScanConfig scanConfig,String applicationName);

    ApiScanVO.ScanConfig loadScanConfig();

    String getScanConfigYaml();

    void saveScanConfig(String configYaml);

    com.baiyi.cratos.domain.DataTable<ApiScanResult> queryScanResults(com.baiyi.cratos.domain.param.http.security.ApiScanParam.ScanResultPageQuery pageQuery);

}
