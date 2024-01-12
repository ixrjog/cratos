package com.baiyi.cratos.service;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.domain.param.user.UserParam;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

/**
 * @Author baiyi
 * @Date 2024/1/10 15:59
 * @Version 1.0
 */
public class UserServiceTest extends BaseUnit {

    @Resource
    private UserService userService;

    @Test
    void test() {
        UserParam.AddUser addUser = UserParam.AddUser.builder()
                .uuid("111122223333")
                .username("baiyi-test")
                .email("xxx@qq.com")
                .displayName("白衣")
                .isActive(true)
                .otp(0)
                .name("白衣")
                .password("123456")
                .source("TEST")
                .build();
        // userService.add(addUser.toTarget());
        System.out.println(userService.getById(1));
    }


    @Test
    void test2() {
        userService.deleteById(100);
    }

}
