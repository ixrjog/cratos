package com.baiyi.cratos.aspect;

import com.baiyi.cratos.annotation.SetSessionUserToParam;
import com.baiyi.cratos.domain.ISetSessionUser;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @Author baiyi
 * @Date 2024/2/23 09:57
 * @Version 1.0
 */
@Slf4j
@Aspect
@Component
public class SetSessionUserToParamAspect {

    @Pointcut(value = "@annotation(com.baiyi.cratos.annotation.SetSessionUserToParam)")
    public void annotationPoint() {
    }

    @Before(value = "@annotation(setSessionUserToParam)")
    public void beforeAdvice(JoinPoint joinPoint, SetSessionUserToParam setSessionUserToParam) {
        Arrays.stream(joinPoint.getArgs())
                .filter(arg -> arg instanceof ISetSessionUser)
                .map(arg -> (ISetSessionUser) arg)
                .forEach(iAuthor -> {
                    Authentication authentication = SecurityContextHolder.getContext()
                            .getAuthentication();
                    iAuthor.setSessionUser(authentication.getName());
                });
    }

}
