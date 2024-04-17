package com.baiyi.cratos.shell.auth.custom;

import com.baiyi.cratos.common.auth.IAuthProvider;
import com.baiyi.cratos.common.auth.factory.AuthProviderFactory;
import com.baiyi.cratos.common.exception.auth.AuthenticationException;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.shell.auth.SshShellAuthenticationProvider;
import com.baiyi.cratos.shell.auth.facade.UserCredFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Optional;

import static com.baiyi.cratos.domain.ErrorEnum.AUTHENTICATION_INVALID_IDENTITY_AUTHENTICATION_PROVIDER_CONFIGURATION;
import static com.baiyi.cratos.domain.ErrorEnum.INCORRECT_USERNAME_OR_PASSWORD;

/**
 * @Author baiyi
 * @Date 2024/4/17 上午11:00
 * @Version 1.0
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class CustomPasswordConfiguration {

    private final UserCredFacade userCredFacade;

    private final UserService userService;

    @Value("${cratos.auth.enabled:true}")
    private Boolean authEnabled;

    @Value("${cratos.auth.provider:LOCAL}")
    private String provider;

    @Bean
    @Primary
    public SshShellAuthenticationProvider passwordAuthenticatorProvider() {
        return (username, pass, serverSession) -> {
            if (!authEnabled) {
                return true;
            }
            try {
                IAuthProvider authProvider = Optional.ofNullable(AuthProviderFactory.getProvider(provider))
                        .orElseThrow(() -> new AuthenticationException(AUTHENTICATION_INVALID_IDENTITY_AUTHENTICATION_PROVIDER_CONFIGURATION));
                User user = Optional.ofNullable(userService.getByUsername(username))
                        .orElseThrow(() -> new AuthenticationException(INCORRECT_USERNAME_OR_PASSWORD));

                return authProvider.verifyPassword(user, pass);
            } catch (AuthenticationException e) {
                return false;
            }

//            try {
//                Credential credential = userCredFacade.getUserPasswordCredential(username);
//                return pass.equals(credential.getCredential());
//            } catch (AuthenticationException e) {
//                return false;
//            }
        };
    }

}
