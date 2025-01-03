package com.baiyi.cratos.facade.auth.impl;

import com.baiyi.cratos.common.enums.AuthProviderEnum;
import com.baiyi.cratos.common.exception.auth.AuthenticationException;
import com.baiyi.cratos.domain.generator.Credential;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.generator.UserToken;
import com.baiyi.cratos.domain.param.http.login.LoginParam;
import com.baiyi.cratos.domain.view.log.LoginVO;
import com.baiyi.cratos.facade.UserTokenFacade;
import com.baiyi.cratos.facade.auth.BaseAuthProvider;
import com.baiyi.cratos.service.BusinessCredentialService;
import com.baiyi.cratos.service.CredentialService;
import com.baiyi.cratos.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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
public class LocalAuthProvider extends BaseAuthProvider {

    private final AuthenticationManager authenticationManager;

    public LocalAuthProvider(UserTokenFacade userTokenFacade, UserService userService,
                             BusinessCredentialService businessCredentialService, CredentialService credentialService,
                             AuthenticationManager authenticationManager) {
        super(userTokenFacade, userService, businessCredentialService, credentialService);
        this.authenticationManager = authenticationManager;
    }

    @Override
    public String getName() {
        return AuthProviderEnum.LOCAL.name();
    }

    @Override
    public LoginVO.Login login(LoginParam.Login loginParam, User user) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                loginParam.getUsername(), loginParam.getPassword());
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        if (authentication == null) {
            throw new AuthenticationException(AUTHENTICATION_FAILED);
        }
        UserToken userToken = revokeAndIssueNewToken(loginParam);
        if (authentication.isAuthenticated()) {
            return LoginVO.Login.builder()
                    .name(user.getDisplayName())
                    .token(userToken.getToken())
                    .uuid(user.getUuid())
                    .build();
        }
        throw new AuthenticationException(AUTHENTICATION_FAILED);
    }

    @Override
    public boolean verifyPassword(User user, String password) {
        if (!StringUtils.hasText(password)) {
            return false;
        }
        Credential credential = getUserPasswordCredential(user);
        return password.equals(credential.getCredential());
    }

}
