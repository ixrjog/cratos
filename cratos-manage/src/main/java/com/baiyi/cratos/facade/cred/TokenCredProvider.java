package com.baiyi.cratos.facade.cred;

import com.baiyi.cratos.common.builder.DictBuilder;
import com.baiyi.cratos.common.cred.CredInjectionNameEnum;
import com.baiyi.cratos.common.enums.CredentialTypeEnum;
import com.baiyi.cratos.domain.generator.Credential;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.facade.cred.base.BaseCredProvider;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/2/29 15:39
 * @Version 1.0
 */
@Component
public class TokenCredProvider extends BaseCredProvider {

    @Override
    public CredentialTypeEnum getType() {
        return CredentialTypeEnum.TOKEN;
    }

    protected CredInjectionNameEnum[] listCredInjectionNameEnums() {
        return new CredInjectionNameEnum[]{CredInjectionNameEnum.CRED_TOKEN};
    }

    @Override
    protected DictBuilder newDictBuilder(Credential credential) {
        String decryptedToken = decrypt(credential.getCredential());
        return DictBuilder.newBuilder()
                .put(ConfigCredTemplate.Names.TOKEN, decryptedToken);
    }

}
