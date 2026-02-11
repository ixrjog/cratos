package com.baiyi.cratos.facade;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.domain.param.http.acme.AcmeAccountParam;
import com.baiyi.cratos.eds.acme.enums.AcmeProviderEnum;
import com.baiyi.cratos.facade.acme.AcmeFacade;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/2/10 09:55
 * &#064;Version 1.0
 */
public class AcmeFacadeTest extends BaseUnit {

    @Resource
    private AcmeFacade acmeFacade;

    @Test
    void test1() {
        AcmeAccountParam.CreateAccount createAccount = AcmeAccountParam.CreateAccount.builder()
                .name("baiyitest")
                .email("jian.liang@palmapy-inc.com")
                .acmeProvider(AcmeProviderEnum.LETSENCRYPT.getProvider())
                .build();
        acmeFacade.createAcmeAccount(createAccount);
    }

}
