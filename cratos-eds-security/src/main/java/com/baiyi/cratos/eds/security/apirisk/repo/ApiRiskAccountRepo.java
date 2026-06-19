package com.baiyi.cratos.eds.security.apirisk.repo;

import com.baiyi.cratos.common.builder.DictBuilder;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.security.apirisk.result.ApiRiskAccountResult;
import com.baiyi.cratos.eds.security.apirisk.result.base.ApiRiskResponse;
import com.baiyi.cratos.eds.security.apirisk.service.ApiRiskService;
import com.baiyi.cratos.eds.security.apirisk.service.ApiRiskServiceFactory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/4/15 14:37
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiRiskAccountRepo {

    public static ApiRiskResponse<ApiRiskAccountResult.Account> listAccount(EdsConfigs.ApiRisk apiRisk) {
        ApiRiskService apiRiskService = ApiRiskServiceFactory.createApiRiskService(apiRisk);
        int page = 1;
        int size = 10;
        Map<String, String> param = DictBuilder.newBuilder()
                .put("page", page)
                .put("size", size)
                .build();
        return apiRiskService.listAccount(param);
    }

}
