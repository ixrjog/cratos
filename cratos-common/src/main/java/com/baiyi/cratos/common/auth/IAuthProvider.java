package com.baiyi.cratos.common.auth;

import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.param.http.login.LoginParam;
import com.baiyi.cratos.domain.view.log.LoginVO;

/**
 * @Author baiyi
 * @Date 2024/1/10 13:42
 * @Version 1.0
 */
public interface IAuthProvider {

    String getName();

    LoginVO.Login login(LoginParam.Login loginParam, User user);

    boolean verifyPassword(User user, String password);

}
