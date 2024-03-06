package com.baiyi.cratos.facade.cred;

import com.baiyi.cratos.common.builder.DictBuilder;
import com.baiyi.cratos.common.cred.CredInjectionNameEnum;
import com.baiyi.cratos.common.enums.CredentialTypeEnum;
import com.baiyi.cratos.domain.generator.Credential;
import com.baiyi.cratos.facade.cred.base.BaseCredProvider;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/3/6 16:15
 * @Version 1.0
 */
@Component
public class UsernameWithPasswordCredProvider extends BaseCredProvider {

    @Override
    public CredentialTypeEnum getType() {
        return CredentialTypeEnum.USERNAME_WITH_PASSWORD;
    }

    protected CredInjectionNameEnum[] listCredInjectionNameEnums() {
        return new CredInjectionNameEnum[]{CredInjectionNameEnum.CRED_USERNAME, CredInjectionNameEnum.CRED_PASSWORD};
    }

    @Override
    protected DictBuilder newDictBuilder(Credential credential) {
        String decryptedPassword = decrypt(credential.getCredential());
        return DictBuilder.newBuilder()
                .put(CredInjectionNameEnum.CRED_USERNAME.name(), credential.getUsername())
                .put(CredInjectionNameEnum.CRED_PASSWORD.name(), decryptedPassword);
    }

}
