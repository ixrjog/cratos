package com.baiyi.cratos.facade.cred.base;

import com.baiyi.cratos.common.builder.DictBuilder;
import com.baiyi.cratos.common.cred.CredInjectionNameEnum;
import com.baiyi.cratos.common.cred.ICredProvider;
import com.baiyi.cratos.common.enums.CredentialTypeEnum;
import com.baiyi.cratos.domain.generator.Credential;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringSubstitutor;
import org.jasypt.encryption.StringEncryptor;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2024/2/29 14:48
 * @Version 1.0
 */
public abstract class BaseCredProvider implements ICredProvider {

    @Resource
    private StringEncryptor stringEncryptor;

    protected static final String CREDENTIAL = "CRED_";

    abstract protected CredInjectionNameEnum[] listCredInjectionNameEnums();

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
            dictBuilder.put(ConfigCredTemplate.Names.SECRET, credential2);
        }
        return renderTemplate(yaml, dictBuilder.build());
    }

    private DictBuilder newDictBuilder(Credential credential) {
        String decryptedCredential = decrypt(credential.getCredential());
        return DictBuilder.newBuilder()
                .put(ConfigCredTemplate.Names.USERNAME, credential.getUsername())
                .put(ConfigCredTemplate.Names.PASSWORD, decryptedCredential)
                .put(ConfigCredTemplate.Names.TOKEN, decryptedCredential)
                .put(ConfigCredTemplate.Names.ACCESS_KEY, decryptedCredential);
    }

    protected String decrypt(String str) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        return stringEncryptor.decrypt(str);
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
