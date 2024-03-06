package com.baiyi.cratos.facade.cred;

import com.baiyi.cratos.common.builder.DictBuilder;
import com.baiyi.cratos.common.cred.CredInjectionNameEnum;
import com.baiyi.cratos.common.enums.CredentialTypeEnum;
import com.baiyi.cratos.domain.generator.Credential;
import com.baiyi.cratos.facade.cred.base.BaseCredProvider;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/2/29 15:19
 * @Version 1.0
 */
@Component
public class SshUsernameWithKeyPairCredProvider extends BaseCredProvider {

    @Override
    public CredentialTypeEnum getType() {
        return CredentialTypeEnum.SSH_USERNAME_WITH_KEY_PAIR;
    }

    protected CredInjectionNameEnum[] listCredInjectionNameEnums() {
        return new CredInjectionNameEnum[]{};
    }

    @Override
    protected DictBuilder newDictBuilder(Credential credential) {
        return DictBuilder.newBuilder();
    }

}
