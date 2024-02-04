package com.baiyi.cratos.facade.validator.impl;

import com.baiyi.cratos.common.enums.CredentialTypeEnum;
import com.baiyi.cratos.domain.generator.Credential;
import com.baiyi.cratos.facade.validator.BaseFingerprintAlgorithm;
import com.baiyi.cratos.facade.validator.CredValidationRules;
import com.baiyi.cratos.facade.validator.ICredentialValidator;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Author baiyi
 * @Date 2024/2/4 11:28
 * @Version 1.0
 */
@Component
public class SshUsernameWithKeyPairCredValidator extends BaseFingerprintAlgorithm implements ICredentialValidator {

    private static final CredValidationRules rules = CredValidationRules.builder()
            .credentialNullMessage("The SSH privateKey must be specified.")
            .verifyCredential2NotNull(true)
            .credential2NullMessage("The SSH publicKey must be specified.")
            .verifyExpiredTime(true)
            .maxExpiredTime(TimeUnit.MILLISECONDS.convert(366L * 5, TimeUnit.DAYS))
            .build();

    public CredentialTypeEnum getType() {
        return CredentialTypeEnum.SSH_USERNAME_WITH_KEY_PAIR;
    }

    @Override
    public void verify(Credential credential) {
        rules.verify(credential);
    }


}
