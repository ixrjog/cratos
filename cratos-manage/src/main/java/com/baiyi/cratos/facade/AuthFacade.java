package com.baiyi.cratos.facade;

import com.baiyi.cratos.domain.param.login.LoginParam;
import com.baiyi.cratos.domain.view.log.LoginVO;

/**
 * @Author baiyi
 * @Date 2024/1/10 11:44
 * @Version 1.0
 */
public interface AuthFacade {

    LoginVO.Login login(LoginParam.Login loginParam);

}
