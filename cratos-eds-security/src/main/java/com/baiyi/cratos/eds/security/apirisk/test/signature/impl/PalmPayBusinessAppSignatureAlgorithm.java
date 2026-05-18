package com.baiyi.cratos.eds.security.apirisk.test.signature.impl;

import com.baiyi.cratos.eds.security.apirisk.test.enums.PrivateKeyType;
import com.baiyi.cratos.eds.security.apirisk.test.enums.SignatureAlgorithmEnum;
import com.baiyi.cratos.service.CredentialService;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/5/13 18:27
 * &#064;Version 1.0
 */
@Component
public class PalmPayBusinessAppSignatureAlgorithm extends FlexiBankAppSignatureAlgorithm {

    public PalmPayBusinessAppSignatureAlgorithm(CredentialService credentialService) {
        super(credentialService);
    }

    @Override
    public SignatureAlgorithmEnum getSignatureAlgorithmEnum() {
        return SignatureAlgorithmEnum.PALMPAYBUSINESSAPPSIGN;
    }

    @Override
    protected String getPrivateKey(PrivateKeyType type) {
        if (PrivateKeyType.RELEASE.equals(type)) {
            return getPrivateKey(262);
        }
        return getPrivateKey(263);
    }

}
