package com.baiyi.cratos.facade.acme;

import com.baiyi.cratos.domain.param.http.acme.AcmeAccountParam;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/2/9 17:44
 * &#064;Version 1.0
 */
public interface AcmeFacade {

    void createAcmeAccount(AcmeAccountParam.CreateAccount createAccount);

}
