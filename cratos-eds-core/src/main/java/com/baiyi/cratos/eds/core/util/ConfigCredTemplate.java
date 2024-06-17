package com.baiyi.cratos.eds.core.util;

import com.baiyi.cratos.common.cred.CredProviderFactory;
import com.baiyi.cratos.common.cred.ICredProvider;
import com.baiyi.cratos.common.exception.CredException;
import com.baiyi.cratos.domain.generator.Credential;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/2/27 14:34
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public final class ConfigCredTemplate {

    private static final String CREDENTIAL = "CRED_";

    public interface Names {
        String USERNAME = CREDENTIAL + "USERNAME";
        String PASSWORD = CREDENTIAL + "PASSWORD";
        String TOKEN = CREDENTIAL + "TOKEN";
        String ACCESS_KEY = CREDENTIAL + "ACCESS_KEY";
        String SECRET = CREDENTIAL + "SECRET";
    }

    /**
     * 渲染模板
     *
     * @param yaml
     * @param credential
     * @return
     */
    public String renderTemplate(String yaml, Credential credential) {
        ICredProvider iCredProvider = CredProviderFactory.getCredProvider(credential.getCredentialType());
        if (iCredProvider == null) {
            throw new CredException("CredType error: {}", credential.getCredentialType());
        }
        return iCredProvider
                .renderTemplate(yaml, credential);
    }

}
