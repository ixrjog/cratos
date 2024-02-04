package com.baiyi.cratos.facade.validator.impl;

import com.baiyi.cratos.common.enums.CredentialTypeEnum;
import com.baiyi.cratos.common.exception.InvalidCredentialException;
import com.baiyi.cratos.domain.generator.Credential;
import com.baiyi.cratos.facade.validator.ICredentialValidator;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/2/4 11:35
 * @Version 1.0
 */
@Component
public class OtpCredValidator implements ICredentialValidator {

    public CredentialTypeEnum getType() {
        return CredentialTypeEnum.OTP;
    }

    @Override
    public void verify(Credential credential) {
        throw new InvalidCredentialException("Manual creation is not supported.");
    }

}
