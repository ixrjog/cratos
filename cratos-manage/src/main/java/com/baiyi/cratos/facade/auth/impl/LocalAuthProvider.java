package com.baiyi.cratos.facade.auth.impl;

import com.baiyi.cratos.common.constants.AuthProviderEnum;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.param.login.LoginParam;
import com.baiyi.cratos.domain.view.log.LoginVO;
import com.baiyi.cratos.facade.auth.BaseAuthProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/1/10 13:41
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class LocalAuthProvider extends BaseAuthProvider {

    @Override
    public String getName() {
        return AuthProviderEnum.LOCAL.name();
    }

    @Override
    public LoginVO.Login login(LoginParam.Login loginParam, User user) {
        return null;
    }

}
