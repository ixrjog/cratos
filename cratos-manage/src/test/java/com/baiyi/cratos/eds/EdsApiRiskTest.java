package com.baiyi.cratos.eds;

import com.baiyi.cratos.domain.util.JSONUtils;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.security.apirisk.repo.ApiRiskAccountRepo;
import org.junit.jupiter.api.Test;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/4/15 14:40
 * &#064;Version 1.0
 */
public class EdsApiRiskTest extends BaseEdsTest<EdsConfigs.ApiRisk> {

    @Test
    void test2() {
        EdsConfigs.ApiRisk apiRisk = getConfig(81);
        System.out.println(JSONUtils.writeValueAsPrettyString(ApiRiskAccountRepo.listAccount(apiRisk)));
    }

}
