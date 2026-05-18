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
 * &#064;Date  2026/5/7 14:12
 * &#064;Version 1.0
 */
@Component
public class PalmPayAppSignatureAlgorithm extends BasePPSignatureAlgorithm {

    public PalmPayAppSignatureAlgorithm(CredentialService credentialService) {
        super(credentialService);
    }

    @Override
    protected Map<String, String> calcSign(GenericCall.Request request, String privateKeyB64, String signData1,
                                           String signData2) {
        if (request.getHeaders()
                .containsKey("appsource")) {
            if ("0".equals(request.getHeaders()
                                   .get("appsource"))) {
                return Map.of("pp_req_sign_v2", sign(signData2, privateKeyB64));
            }
        }
        return Map.of("pp_req_sign", sign(signData1, privateKeyB64), "pp_req_sign_2", sign(signData2, privateKeyB64));
    }

    @Override
    protected String getPrivateKey(PrivateKeyType type) {
        if (PrivateKeyType.RELEASE.equals(type)) {
            return getPrivateKey(255);
        }
        return getPrivateKey(254);
    }

    @Override
    public SignatureAlgorithmEnum getSignatureAlgorithmEnum() {
        return SignatureAlgorithmEnum.PALMPAYAPPSIGN;
    }

}
