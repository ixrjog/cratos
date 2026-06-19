package com.baiyi.cratos.eds;

import com.baiyi.cratos.domain.util.JSONUtils;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.security.apirisk.repo.ApiRiskAccountRepo;
import com.baiyi.cratos.eds.security.apirisk.repo.ApiRiskApiRepo;
import com.baiyi.cratos.eds.security.apirisk.repo.ApiRiskWeaknessRepo;
import com.baiyi.cratos.eds.security.apirisk.result.ApiRiskApiResult;
import com.baiyi.cratos.eds.security.apirisk.result.ApiRiskWeaknessResult;
import com.baiyi.cratos.eds.security.apirisk.result.base.ApiRiskResponse;
import org.junit.jupiter.api.Test;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/4/15 14:40
 * &#064;Version 1.0
 */
public class EdsApiRiskTest extends BaseEdsTest<EdsConfigs.ApiRisk> {

    @Test
    void test1() {
        EdsConfigs.ApiRisk apiRisk = getConfig(81);
        System.out.println(JSONUtils.writeValueAsPrettyString(ApiRiskAccountRepo.listAccount(apiRisk)));
    }

    @Test
    void test2() {
        EdsConfigs.ApiRisk apiRisk = getConfig(81);
        ApiRiskResponse<ApiRiskApiResult.Api> httpApiApiRiskResponse =  ApiRiskApiRepo.listApi(apiRisk, 1, 25);

        System.out.println(JSONUtils.writeValueAsPrettyString(httpApiApiRiskResponse ));
    }

    @Test
    void test3() {
        EdsConfigs.ApiRisk apiRisk = getConfig(81);

        ApiRiskResponse<ApiRiskWeaknessResult.Weakness> response = ApiRiskWeaknessRepo.listWeakness(apiRisk, 1, 25);
        System.out.println(JSONUtils.writeValueAsPrettyString(response));

    }

}
