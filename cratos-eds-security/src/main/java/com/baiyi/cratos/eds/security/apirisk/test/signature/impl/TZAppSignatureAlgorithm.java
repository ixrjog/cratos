package com.baiyi.cratos.eds.security.apirisk.test.signature.impl;

import com.baiyi.cratos.eds.security.apirisk.test.enums.PrivateKeyType;
import com.baiyi.cratos.eds.security.apirisk.test.enums.SignatureAlgorithmEnum;
import com.baiyi.cratos.eds.security.apirisk.test.model.GenericCall;
import com.baiyi.cratos.eds.security.apirisk.test.signature.impl.base.BasePPSignatureAlgorithm;
import com.baiyi.cratos.service.CredentialService;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/5/20 11:22
 * &#064;Version 1.0
 */
@Component
public class TZAppSignatureAlgorithm extends BasePPSignatureAlgorithm {

    public TZAppSignatureAlgorithm(CredentialService credentialService) {
        super(credentialService);
    }

    @Override
    protected Map<String, String> calcSign(GenericCall.Request request, String privateKeyB64, String signData1,
                                           String signData2) {
        return Map.of("pp_req_sign_v2", sign(signData2, privateKeyB64));
    }

    @Override
    protected String getPrivateKey(PrivateKeyType type) {
        return getPrivateKey(267);
    }

    @Override
    public SignatureAlgorithmEnum getSignatureAlgorithmEnum() {
        return SignatureAlgorithmEnum.TZAPPSIGN;
    }

}