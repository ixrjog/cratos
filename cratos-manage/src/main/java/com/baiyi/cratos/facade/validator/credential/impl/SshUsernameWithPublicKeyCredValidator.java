package com.baiyi.cratos.facade.validator.credential.impl;

import com.baiyi.cratos.common.annotation.Expires;
import com.baiyi.cratos.common.enums.CredentialTypeEnum;
import com.baiyi.cratos.common.exception.InvalidCredentialException;
import com.baiyi.cratos.domain.generator.Credential;
import com.baiyi.cratos.facade.validator.credential.BaseFingerprintAlgorithm;
import com.baiyi.cratos.facade.validator.credential.CredValidationRules;
import com.baiyi.cratos.facade.validator.credential.ICredentialValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Author baiyi
 * @Date 2024/4/19 下午5:00
 * @Version 1.0
 */
@Component
@Expires(termOfValidity = 366L * 5, unit = TimeUnit.DAYS)
public class SshUsernameWithPublicKeyCredValidator extends BaseFingerprintAlgorithm implements ICredentialValidator {

    @Value("${cratos.credential.highSecurity:false}")
    private boolean highSecurity;

    private static final CredValidationRules rules = CredValidationRules.builder()
            .credentialNullMessage("The SSH pubKey must be specified.")
            .verifyExpiredTime(true)
            .maxExpiredTime(TimeUnit.MILLISECONDS.convert(366L * 5, TimeUnit.DAYS))
            .build();

    public CredentialTypeEnum getType() {
        return CredentialTypeEnum.SSH_USERNAME_WITH_PUBLIC_KEY;
    }

    public void calcAndFillInFingerprint(Credential credential) {
        String fingerprint = calcFingerprint(credential.getCredential());
        credential.setFingerprint(fingerprint);
    }

    @Override
    public void verify(Credential credential) {
        if (highSecurity) {
            throw new InvalidCredentialException("The platform has enabled high security and uses key \"Ed25519\" pairs.");
        }
        rules.verify(credential);
    }

}
