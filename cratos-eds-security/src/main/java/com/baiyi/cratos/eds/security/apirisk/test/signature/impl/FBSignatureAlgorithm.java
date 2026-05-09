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
 * &#064;Date  2026/5/7 16:58
 * &#064;Version 1.0
 */
@Component
public class FBSignatureAlgorithm extends BasePPSignatureAlgorithm {

    public FBSignatureAlgorithm(CredentialService credentialService) {
        super(credentialService);
    }

    @Override
    protected Map<String, String> calcSign(GenericCall.Request request, String privateKeyB64, String signData1,
                                           String signData2) {
        return Map.of("pp_req_sign_v2", sign(signData2, privateKeyB64));
    }

    @Override
    protected String getPrivateKey(PrivateKeyType type) {
        if (PrivateKeyType.RELEASE.equals(type)) {
            return getPrivateKey(253);
        }
        return getPrivateKey(252);
    }

    @Override
    public SignatureAlgorithmEnum getSignatureAlgorithmEnum() {
        return SignatureAlgorithmEnum.FLEXIBANKAPPSIGN;
    }

}
