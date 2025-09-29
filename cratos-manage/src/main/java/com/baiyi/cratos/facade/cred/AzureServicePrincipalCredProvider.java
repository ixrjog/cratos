package com.baiyi.cratos.facade.cred;

import com.baiyi.cratos.common.builder.DictBuilder;
import com.baiyi.cratos.common.cred.CredInjectionNameEnum;
import com.baiyi.cratos.common.enums.CredentialTypeEnum;
import com.baiyi.cratos.domain.generator.Credential;
import com.baiyi.cratos.facade.cred.base.BaseCredProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/9/29 11:25
 * &#064;Version 1.0
 */
@Slf4j
@Component
public class AzureServicePrincipalCredProvider extends BaseCredProvider {

    @Override
    public CredentialTypeEnum getType() {
        return CredentialTypeEnum.AZURE_SERVICE_PRINCIPAL;
    }

    protected CredInjectionNameEnum[] listCredInjectionNameEnums() {
        return new CredInjectionNameEnum[]{CredInjectionNameEnum.CRED_CLIENT_ID, CredInjectionNameEnum.CRED_CLIENT_SECRET};
    }

    @Override
    protected DictBuilder newDictBuilder(Credential credential) {
        String clientId = decrypt(credential.getCredential());
        String clientSecret = decrypt(credential.getCredential2());
        return DictBuilder.newBuilder()
                .put(CredInjectionNameEnum.CRED_CLIENT_ID.name(), clientId)
                .put(CredInjectionNameEnum.CRED_CLIENT_SECRET.name(), clientSecret);
    }

}
