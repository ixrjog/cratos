package com.baiyi.cratos.facade.validator.credential.impl;

import com.baiyi.cratos.common.annotation.Expires;
import com.baiyi.cratos.common.enums.CredentialTypeEnum;
import com.baiyi.cratos.common.exception.InvalidCredentialException;
import com.baiyi.cratos.domain.generator.Credential;
import com.baiyi.cratos.facade.validator.credential.BaseFingerprintAlgorithm;
import com.baiyi.cratos.facade.validator.credential.CredValidationRules;
import com.baiyi.cratos.facade.validator.credential.BaseCredentialValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Author baiyi
 * @Date 2024/2/4 11:26
 * @Version 1.0
 */
@Component
@Expires(termOfValidity = 366L * 5, unit = TimeUnit.DAYS)
public class SshUsernameWithPrivateKeyCredValidator extends BaseFingerprintAlgorithm implements BaseCredentialValidator {

    @Value("${cratos.credential.highSecurity:false}")
    private boolean highSecurity;

    private static final CredValidationRules rules = CredValidationRules.builder()
            .credentialNullMessage("The SSH privateKey must be specified.")
            .verifyExpiredTime(true)
            .maxExpiredTime(TimeUnit.MILLISECONDS.convert(366L * 5, TimeUnit.DAYS))
            .build();

    public CredentialTypeEnum getType() {
        return CredentialTypeEnum.SSH_USERNAME_WITH_PRIVATE_KEY;
    }

    @Override
    public void calcAndFillInFingerprint(Credential credential) {
        // Private key不能计算指纹
        credential.setFingerprint("-");
    }

    @Override
    public void verify(Credential credential) {
        if (highSecurity) {
            throw new InvalidCredentialException("The platform has enabled high security and uses key \"Ed25519\" pairs.");
        }
        rules.verify(credential);
    }

}
