package com.baiyi.cratos.eds.security.apirisk.test.signature;

import com.baiyi.cratos.eds.security.apirisk.test.enums.PrivateKeyType;
import com.baiyi.cratos.eds.security.apirisk.test.enums.SignatureAlgorithmEnum;
import com.baiyi.cratos.eds.security.apirisk.test.model.GenericCall;
import org.springframework.beans.factory.InitializingBean;

import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/5/7 13:40
 * &#064;Version 1.0
 */
public interface SignatureAlgorithm extends InitializingBean {

    Map<String, String> calcSign(GenericCall.Request request, PrivateKeyType type);

    SignatureAlgorithmEnum getSignatureAlgorithmEnum();

    default void afterPropertiesSet() throws Exception {
        SignatureFactory.register(this);
    }

}
