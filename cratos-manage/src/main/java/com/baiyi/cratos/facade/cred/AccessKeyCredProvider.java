package com.baiyi.cratos.facade.cred;

import com.baiyi.cratos.common.builder.DictBuilder;
import com.baiyi.cratos.common.cred.CredInjectionNameEnum;
import com.baiyi.cratos.common.enums.CredentialTypeEnum;
import com.baiyi.cratos.domain.generator.Credential;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.baiyi.cratos.facade.cred.base.BaseCredProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/2/29 14:45
 * @Version 1.0
 */
@Slf4j
@Component
public class AccessKeyCredProvider extends BaseCredProvider {

    @Override
    public CredentialTypeEnum getType() {
        return CredentialTypeEnum.ACCESS_KEY;
    }

    protected CredInjectionNameEnum[] listCredInjectionNameEnums() {
        return new CredInjectionNameEnum[]{CredInjectionNameEnum.CRED_USERNAME, CredInjectionNameEnum.CRED_ACCESS_KEY, CredInjectionNameEnum.CRED_SECRET};
    }

    protected DictBuilder newDictBuilder(Credential credential) {
        String decryptedAccessKey = decrypt(credential.getCredential());
        String decryptedSecret = decrypt(credential.getCredential2());
        return DictBuilder.newBuilder()
                .put(ConfigCredTemplate.Names.USERNAME, credential.getUsername())
                .put(ConfigCredTemplate.Names.ACCESS_KEY, decryptedAccessKey)
                .put(ConfigCredTemplate.Names.SECRET, decryptedSecret);
    }

}
