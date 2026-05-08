package com.baiyi.cratos.facade;

import com.baiyi.cratos.domain.param.http.security.ApiTestParam;
import com.baiyi.cratos.eds.security.apirisk.test.model.GenericCall;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/5/7 11:22
 * &#064;Version 1.0
 */
public interface ApiSecurityTestFacade {

    GenericCall.Response  callTestApi(ApiTestParam.CallApi callApi);

}
