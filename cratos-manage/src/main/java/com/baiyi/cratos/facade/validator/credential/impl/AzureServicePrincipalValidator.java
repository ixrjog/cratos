package com.baiyi.cratos.facade.validator.credential.impl;

import com.baiyi.cratos.common.annotation.Expires;
import com.baiyi.cratos.common.enums.CredentialTypeEnum;
import com.baiyi.cratos.domain.generator.Credential;
import com.baiyi.cratos.facade.validator.credential.CredValidationRules;
import com.baiyi.cratos.facade.validator.credential.BaseCredentialValidator;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/9/29 10:59
 * &#064;Version 1.0
 */
@Component
@Expires(termOfValidity = 366L * 5, unit = TimeUnit.DAYS)
public class AzureServicePrincipalValidator implements BaseCredentialValidator {

    private static final CredValidationRules rules = CredValidationRules.builder()
            .credentialNullMessage("The Azure Service Principal appId must be specified.")
            .verifyCredentialNotNull(true)
            .credential2NullMessage("The Azure Service Principal password must be specified.")
            .verifyCredential2NotNull(true)
            .verifyExpiredTime(true)
            .maxExpiredTime(TimeUnit.MILLISECONDS.convert(366L * 5, TimeUnit.DAYS))
            .build();

    public CredentialTypeEnum getType() {
        return CredentialTypeEnum.AZURE_SERVICE_PRINCIPAL;
    }

    @Override
    public void verify(Credential credential) {
        rules.verify(credential);
    }

}
