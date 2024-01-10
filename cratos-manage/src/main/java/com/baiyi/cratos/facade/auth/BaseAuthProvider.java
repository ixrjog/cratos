package com.baiyi.cratos.facade.auth;

import com.baiyi.cratos.facade.auth.factory.AuthProviderFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * @Author baiyi
 * @Date 2024/1/10 13:46
 * @Version 1.0
 */
public abstract class BaseAuthProvider implements IAuthProvider, InitializingBean {

    @Override
    public void afterPropertiesSet() {
        AuthProviderFactory.register(this);
    }

}
