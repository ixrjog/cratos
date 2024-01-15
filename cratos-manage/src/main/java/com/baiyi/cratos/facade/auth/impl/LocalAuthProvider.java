package com.baiyi.cratos.facade.auth.impl;

import com.baiyi.cratos.common.constants.AuthProviderEnum;
import com.baiyi.cratos.common.exception.auth.AuthenticationException;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.param.login.LoginParam;
import com.baiyi.cratos.domain.view.log.LoginVO;
import com.baiyi.cratos.facade.auth.BaseAuthProvider;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.baiyi.cratos.domain.ErrorEnum.AUTHENTICATION_FAILED;

/**
 * 本地址用户名密码认证
 *
 * @Author baiyi
 * @Date 2024/1/10 13:41
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LocalAuthProvider extends BaseAuthProvider {

    @Resource
    private AuthenticationManager authenticationManager;

    @Override
    public String getName() {
        return AuthProviderEnum.LOCAL.name();
    }

    @Override
    public LoginVO.Login login(LoginParam.Login loginParam, User user) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(loginParam.getUsername(), loginParam.getPassword());
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        if (authentication == null) {
            throw new AuthenticationException(AUTHENTICATION_FAILED);
        }
        // (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        String token = UUID.randomUUID().toString();
        if (authentication.isAuthenticated()) {
            return LoginVO.Login.builder()
                    .name(user.getDisplayName())
                    .token(token)
                    .uuid(user.getUuid())
                    .build();
        }
        throw new AuthenticationException(AUTHENTICATION_FAILED);
    }

}
