package com.baiyi.cratos.eds.security.apirisk.test.signature.impl;

import com.baiyi.cratos.eds.security.apirisk.test.enums.PrivateKeyType;
import com.baiyi.cratos.eds.security.apirisk.test.enums.SignatureAlgorithmEnum;
import com.baiyi.cratos.eds.security.apirisk.test.model.GenericCall;
import com.baiyi.cratos.eds.security.apirisk.test.signature.BaseSignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 不签名
 * &#064;Author  baiyi
 * &#064;Date  2026/5/9 09:47
 * &#064;Version 1.0
 */
@Component
public class NoneSignatureAlgorithm extends BaseSignatureAlgorithm {

    @Override
    public Map<String, String> calcSign(GenericCall.Request request, PrivateKeyType type) {
        return Map.of();
    }

    @Override
    public SignatureAlgorithmEnum getSignatureAlgorithmEnum() {
        return SignatureAlgorithmEnum.NONE;
    }

}
