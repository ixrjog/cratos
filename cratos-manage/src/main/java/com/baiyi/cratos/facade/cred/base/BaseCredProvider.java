package com.baiyi.cratos.facade.cred.base;

import com.baiyi.cratos.common.builder.DictBuilder;
import com.baiyi.cratos.common.cred.CredInjectionNameEnum;
import com.baiyi.cratos.common.cred.ICredProvider;
import com.baiyi.cratos.common.enums.CredentialTypeEnum;
import com.baiyi.cratos.domain.generator.Credential;
import com.baiyi.cratos.eds.core.util.ConfigCredTemplate;
import com.google.common.base.Joiner;
import org.apache.commons.text.StringSubstitutor;

import java.util.Arrays;
import java.util.Map;

import static com.baiyi.cratos.domain.constant.Global.NOT_APPLICABLE;

/**
 * @Author baiyi
 * @Date 2024/2/29 14:48
 * @Version 1.0
 */
public abstract class BaseCredProvider implements ICredProvider {

    abstract protected CredInjectionNameEnum[] listCredInjectionNameEnums();

    @Override
    public String getDesc() {
        if (listCredInjectionNameEnums().length == 0) {
            return NOT_APPLICABLE;
        } else {
            return Joiner.on("、")
                    .join(Arrays.stream(listCredInjectionNameEnums())
                            .map(e -> "${" + e.name() + "}")
                            .toList());
        }
    }

    /**
     * 渲染模板
     *
     * @param yaml
     * @param credential
     * @return
     */
    @Override
    public String renderTemplate(String yaml, Credential credential) {
        DictBuilder dictBuilder = newDictBuilder(credential);
        if (CredentialTypeEnum.ACCESS_KEY.name()
                .equals(credential.getCredentialType())) {
            String credential2 = decrypt(credential.getCredential2());
            dictBuilder.put(ConfigCredTemplate.Names.SECRET, credential2);
        }
        return renderTemplate(yaml, dictBuilder.build());
    }

    abstract protected DictBuilder newDictBuilder(Credential credential);

    protected String decrypt(String str) {
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
