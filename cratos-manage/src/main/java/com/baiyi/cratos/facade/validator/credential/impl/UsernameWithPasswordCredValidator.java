package com.baiyi.cratos.facade.validator.credential.impl;

import com.baiyi.cratos.common.enums.CredentialTypeEnum;
import com.baiyi.cratos.domain.generator.Credential;
import com.baiyi.cratos.facade.validator.credential.CredValidationRules;
import com.baiyi.cratos.facade.validator.credential.ICredentialValidator;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Author baiyi
 * @Date 2024/2/4 10:41
 * @Version 1.0
 */
@Component
public class UsernameWithPasswordCredValidator implements ICredentialValidator {

    private static final CredValidationRules rules = CredValidationRules.builder()
            .credentialNullMessage("The password must be specified.")
            .verifyExpiredTime(true)
            .maxExpiredTime(TimeUnit.MILLISECONDS.convert(90L, TimeUnit.DAYS))
            .build();

    public CredentialTypeEnum getType() {
        return CredentialTypeEnum.USERNAME_WITH_PASSWORD;
    }

    @Override
    public void verify(Credential credential) {
        rules.verify(credential);
    }

}