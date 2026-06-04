package com.baiyi.cratos.eds.security.apirisk.service;

import com.baiyi.cratos.eds.security.apirisk.result.ApiRiskAccountResult;
import com.baiyi.cratos.eds.security.apirisk.result.base.ApiRiskResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/4/15 14:18
 * &#064;Version 1.0
 */
@HttpExchange(accept = "application/json")
public interface ApiRiskService {

    @PostExchange("/audit-apiv2/openApi/v3/account/list")
    ApiRiskResponse<ApiRiskAccountResult.Account> listAccount(@RequestBody Map<String, String> param);

}
