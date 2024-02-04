package com.baiyi.cratos.facade.validator;

import com.baiyi.cratos.common.enums.CredentialTypeEnum;
import com.baiyi.cratos.domain.generator.Credential;
import org.springframework.beans.factory.InitializingBean;

/**
 * @Author baiyi
 * @Date 2024/2/4 10:34
 * @Version 1.0
 */
public interface ICredentialValidator extends InitializingBean {

    CredentialTypeEnum getType();

    void verify(Credential credential);

    default void afterPropertiesSet() {
        CredentialValidatorFactory.register(this);
    }

}
