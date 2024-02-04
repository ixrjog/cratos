package com.baiyi.cratos.facade.validator.credential.impl;

import com.baiyi.cratos.common.enums.CredentialTypeEnum;
import com.baiyi.cratos.domain.generator.Credential;
import com.baiyi.cratos.facade.validator.credential.CredValidationRules;
import com.baiyi.cratos.facade.validator.credential.ICredentialValidator;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Author baiyi
 * @Date 2024/2/4 11:41
 * @Version 1.0
 */
@Component
public class AccessKeyCredValidator implements ICredentialValidator {

    private static final CredValidationRules rules = CredValidationRules.builder()
            .credentialNullMessage("The accessKeyId must be specified.")
            .verifyCredential2NotNull(true)
            .credential2NullMessage("The secretKey must be specified.")
            .verifyExpiredTime(true)
            .maxExpiredTime(TimeUnit.MILLISECONDS.convert(366L * 5, TimeUnit.DAYS))
            .build();

    public CredentialTypeEnum getType() {
        return CredentialTypeEnum.ACCESS_KEY;
    }

    @Override
    public void verify(Credential credential) {
        rules.verify(credential);
    }

}

