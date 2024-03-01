package com.baiyi.cratos.eds.core.util;

import com.baiyi.cratos.common.builder.DictBuilder;
import com.baiyi.cratos.common.enums.CredentialTypeEnum;
import com.baiyi.cratos.domain.generator.Credential;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.StringSubstitutor;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2024/2/27 14:34
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class ConfigCredTemplate {

    private final StringEncryptor stringEncryptor;

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
        DictBuilder dictBuilder = newDictBuilder(credential);
        if (CredentialTypeEnum.ACCESS_KEY.name()
                .equals(credential.getCredentialType())) {
            String credential2 = decrypt(credential.getCredential2());
            dictBuilder.put(Names.SECRET, credential2);
        }
        return renderTemplate(yaml, dictBuilder.build());
    }

    private DictBuilder newDictBuilder(Credential credential) {
        String decryptedCredential = decrypt(credential.getCredential());
        return DictBuilder.newBuilder()
                .put(Names.USERNAME, credential.getUsername())
                .put(Names.PASSWORD, decryptedCredential)
                .put(Names.TOKEN, decryptedCredential)
                .put(Names.ACCESS_KEY, decryptedCredential);
    }

    private String decrypt(String str) {
        // 无需要解密,AOP实现
//        if (StringUtils.isEmpty(str)) {
//            return null;
//        }
//        return stringEncryptor.decrypt(str);
        return str;
    }

    private String renderTemplate(String templateString, Map<String, String> variable) {
        try {
            StringSubstitutor sub = new StringSubstitutor(variable);
            return sub.replace(templateString);
        } catch (Exception e) {
            return templateString;
        }
    }

}
