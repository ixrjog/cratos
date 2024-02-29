package com.baiyi.cratos.common.cred;

import com.baiyi.cratos.common.enums.CredentialTypeEnum;
import org.springframework.beans.factory.InitializingBean;

/**
 * @Author baiyi
 * @Date 2024/2/29 14:36
 * @Version 1.0
 */
public interface ICredProvider extends InitializingBean {

    CredentialTypeEnum getType();

    default void afterPropertiesSet() {
        CredProviderFactory.register(this);
    }

}
