package com.baiyi.cratos.facade.validator.credential.impl;

import com.baiyi.cratos.common.enums.CredentialTypeEnum;
import com.baiyi.cratos.common.exception.InvalidCredentialException;
import com.baiyi.cratos.domain.generator.Credential;
import com.baiyi.cratos.facade.validator.credential.BaseFingerprintAlgorithm;
import com.baiyi.cratos.facade.validator.credential.CredValidationRules;
import com.baiyi.cratos.facade.validator.credential.ICredentialValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * @Author baiyi
 * @Date 2024/2/4 11:28
 * @Version 1.0
 */
@Component
public class SshUsernameWithKeyPairCredValidator extends BaseFingerprintAlgorithm implements ICredentialValidator {

    @Value("${cratos.credential.highSecurity:false}")
    private boolean highSecurity;

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
        if (highSecurity) {
            if (!StringUtils.hasText(credential.getCredential2()) || !credential.getCredential2()
                    .startsWith("ssh-ed25519")) {
                throw new InvalidCredentialException("The current credential are not secure, please use Ed25519.");
            }
        }
        rules.verify(credential);
    }

}
