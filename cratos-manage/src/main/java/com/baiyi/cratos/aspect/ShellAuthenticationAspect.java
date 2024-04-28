package com.baiyi.cratos.aspect;

import com.baiyi.cratos.facade.RbacFacade;
import com.baiyi.cratos.shell.SshShellHelper;
import com.baiyi.cratos.shell.annotation.ShellAuthentication;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import static com.baiyi.cratos.domain.ErrorEnum.AUTHENTICATION_FAILED;

/**
 * @Author baiyi
 * @Date 2024/4/24 下午5:44
 * @Version 1.0
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
@Order(-1)
public class ShellAuthenticationAspect {

    private final SshShellHelper helper;

    private final RbacFacade rbacFacade;

    private static final String SHELL_PREFIX = "/shell";

    @Pointcut(value = "@annotation(com.baiyi.cratos.shell.annotation.ShellAuthentication)")
    public void annotationPoint() {
    }

    @Before(value = "@annotation(shellAuthentication)")
    public void beforeAdvice(JoinPoint joinPoint, ShellAuthentication shellAuthentication) {
        String username = helper.getSshSession()
                .getUsername();
        if (!StringUtils.hasText(username)) {
            helper.printError("Authentication failed.");
            throw new com.baiyi.cratos.common.exception.auth.AuthenticationException(AUTHENTICATION_FAILED);
        }
        final String resource = shellAuthentication.resource()
                .startsWith(
                        SHELL_PREFIX + "/") ? shellAuthentication.resource() : SHELL_PREFIX + shellAuthentication.resource();
        rbacFacade.verifyResourceAccessPermissionsForUsername(username, resource);
    }

}
