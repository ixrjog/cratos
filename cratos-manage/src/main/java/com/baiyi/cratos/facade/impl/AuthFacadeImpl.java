package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.common.exception.auth.AuthenticationException;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.param.login.LoginParam;
import com.baiyi.cratos.domain.view.log.LoginVO;
import com.baiyi.cratos.facade.AuthFacade;
import com.baiyi.cratos.facade.UserTokenFacade;
import com.baiyi.cratos.common.auth.IAuthProvider;
import com.baiyi.cratos.common.auth.factory.AuthProviderFactory;
import com.baiyi.cratos.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Optional;

import static com.baiyi.cratos.domain.ErrorEnum.AUTHENTICATION_INVALID_IDENTITY_AUTHENTICATION_PROVIDER_CONFIGURATION;
import static com.baiyi.cratos.domain.ErrorEnum.INCORRECT_USERNAME_OR_PASSWORD;

/**
 * @Author baiyi
 * @Date 2024/1/10 11:45
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthFacadeImpl implements AuthFacade {

    @Value("${cratos.auth.provider:LOCAL}")
    private String provider;

    private final UserService userService;

    private final UserTokenFacade userTokenFacade;

    @Override
    public LoginVO.Login login(LoginParam.Login loginParam) {
        IAuthProvider authProvider = Optional.ofNullable(AuthProviderFactory.getProvider(provider))
                .orElseThrow(() -> new AuthenticationException(AUTHENTICATION_INVALID_IDENTITY_AUTHENTICATION_PROVIDER_CONFIGURATION));

        User user = Optional.ofNullable(userService.getByUsername(loginParam.getUsername()))
                .orElseThrow(() -> new AuthenticationException(INCORRECT_USERNAME_OR_PASSWORD));

        LoginVO.Login login = authProvider.login(loginParam, user);
        // 更新用户登录信息
        User updateUser = User.builder()
                .id(user.getId())
                .lastLogin(new Date())
                .build();
        userService.updateByPrimaryKeySelective(updateUser);
        login.setUsername(loginParam.getUsername());
        return login;
    }

    @Override
    public void logout() {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        String username = authentication.getName();
        if (StringUtils.hasText(username)) {
            userTokenFacade.logout(username);
        }
    }

}
