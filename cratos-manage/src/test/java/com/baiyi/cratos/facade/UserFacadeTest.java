package com.baiyi.cratos.facade;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.domain.param.http.user.UserParam;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

/**
 * @Author baiyi
 * @Date 2024/1/26 15:57
 * @Version 1.0
 */
public class UserFacadeTest extends BaseUnit {

    @Resource
    private UserFacade userFacade;

    @Test
    void test() {
        UserParam.ResetPassword resetPassword = UserParam.ResetPassword.builder()
                .password("123456")
                .build();
        userFacade.resetUserPassword("baiyi-test", resetPassword);
    }

}
