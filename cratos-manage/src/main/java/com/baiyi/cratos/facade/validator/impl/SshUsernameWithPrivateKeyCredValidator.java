package com.baiyi.cratos.facade.validator.impl;

import com.baiyi.cratos.common.enums.CredentialTypeEnum;
import com.baiyi.cratos.domain.generator.Credential;
import com.baiyi.cratos.facade.validator.CredValidationRules;
import com.baiyi.cratos.facade.validator.ICredentialValidator;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Author baiyi
 * @Date 2024/2/4 11:26
 * @Version 1.0
 */
@Component
public class SshUsernameWithPrivateKeyCredValidator implements ICredentialValidator {

    private static final CredValidationRules rules = CredValidationRules.builder()
            .credentialNullMessage("The SSH privateKey must be specified.")
            .verifyExpiredTime(true)
            .maxExpiredTime(TimeUnit.MILLISECONDS.convert(366L * 5, TimeUnit.DAYS))
            .build();

    public CredentialTypeEnum getType() {
        return CredentialTypeEnum.SSH_USERNAME_WITH_PRIVATE_KEY;
    }

    @Override
    public void verify(Credential credential) {
        rules.verify(credential);
    }

}
