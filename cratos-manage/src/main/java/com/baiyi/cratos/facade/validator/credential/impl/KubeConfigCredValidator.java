package com.baiyi.cratos.facade.validator.credential.impl;

import com.baiyi.cratos.common.annotation.Expires;
import com.baiyi.cratos.common.enums.CredentialTypeEnum;
import com.baiyi.cratos.domain.generator.Credential;
import com.baiyi.cratos.facade.validator.credential.CredValidationRules;
import com.baiyi.cratos.facade.validator.credential.BaseCredentialValidator;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Author baiyi
 * @Date 2024/2/4 11:33
 * @Version 1.0
 */
@Component
@Expires(termOfValidity = 366L * 5, unit = TimeUnit.DAYS)
public class KubeConfigCredValidator implements BaseCredentialValidator {

    private static final CredValidationRules rules = CredValidationRules.builder()
            .credentialNullMessage("The kubeconfig must be specified.")
            .verifyExpiredTime(true)
            .maxExpiredTime(TimeUnit.MILLISECONDS.convert(366L * 5, TimeUnit.DAYS))
            .build();

    public CredentialTypeEnum getType() {
        return CredentialTypeEnum.KUBE_CONFIG;
    }

    @Override
    public void verify(Credential credential) {
        rules.verify(credential);
    }

}
