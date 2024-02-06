package com.baiyi.cratos.service.factory.credential;

import com.baiyi.cratos.domain.util.BeanNameConverter;
import org.springframework.beans.factory.InitializingBean;

/**
 * @Author baiyi
 * @Date 2024/2/6 09:57
 * @Version 1.0
 */
public interface ICredentialHolder extends InitializingBean {

    int countByCredentialId(int credentialId);

    default String getName() {
        return BeanNameConverter.serviceImplNameToMapperName(this.getClass()
                .getSimpleName());
    }

    // CredentialHolderFactory.register(this);
    void afterPropertiesSet();

}
