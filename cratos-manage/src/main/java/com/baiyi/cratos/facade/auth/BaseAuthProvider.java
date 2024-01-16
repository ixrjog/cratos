package com.baiyi.cratos.facade.auth;

import com.baiyi.cratos.domain.generator.UserToken;
import com.baiyi.cratos.domain.param.login.LoginParam;
import com.baiyi.cratos.facade.UserTokenFacade;
import com.baiyi.cratos.facade.auth.factory.AuthProviderFactory;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.InitializingBean;

/**
 * @Author baiyi
 * @Date 2024/1/10 13:46
 * @Version 1.0
 */

public abstract class BaseAuthProvider implements IAuthProvider, InitializingBean {

    @Resource
    private UserTokenFacade userTokenFacade;

    @Override
    public void afterPropertiesSet() {
        AuthProviderFactory.register(this);
    }

    protected UserToken revokeAndIssueNewToken(LoginParam.Login loginParam) {
        return userTokenFacade.revokeAndIssueNewToken(loginParam.getUsername());
    }

//    protected String toJwt(UserToken userToken){
//        SecretKey key = Jwts.SIG.HS256.key().build();
//        return Jwts.builder().subject(userToken.getToken()).signWith(key).compact();
//    }

}
