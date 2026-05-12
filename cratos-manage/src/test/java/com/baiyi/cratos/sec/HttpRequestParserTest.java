package com.baiyi.cratos.sec;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.domain.param.http.security.ApiTestParam;
import com.baiyi.cratos.eds.security.apirisk.test.enums.PrivateKeyType;
import com.baiyi.cratos.eds.security.apirisk.test.enums.SignatureAlgorithmEnum;
import com.baiyi.cratos.facade.ApiSecurityTestFacade;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/5/6 11:16
 * &#064;Version 1.0
 */
public class HttpRequestParserTest extends BaseUnit {

    @Resource
    private ApiSecurityTestFacade apiSecurityTestFacade;

    private static final String s = """
            """;


    @Test
    void test1() {
        ApiTestParam.CallApi callApi = ApiTestParam.CallApi.builder()
                .requestMessage(s)
                .convertToHTTPS(true)
                .privateKeyType(PrivateKeyType.DEBUG.name())
                .signatureAlgorithm(SignatureAlgorithmEnum.PALMPAYAPPSIGN.name())
                .ppToken("xxxxx")
                .build();
        apiSecurityTestFacade.callTestApi(callApi);
    }

}
