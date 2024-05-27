package com.baiyi.cratos.shell.aspect;

import com.baiyi.cratos.shell.annotation.ClearScreen;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/23 下午5:24
 * &#064;Version 1.0
 */
@Slf4j
@Aspect
@Component
public class ClearScreenAspect {

    private Terminal terminal;

    @Autowired
    @Lazy
    public void setTerminal(Terminal terminal) {
        this.terminal = terminal;
    }

    @Pointcut(value = "@annotation(com.baiyi.cratos.shell.annotation.ClearScreen)")
    public void annotationPoint() {
    }

    @Before("annotationPoint()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
    }

    @Around("@annotation(screenClear)")
    public Object around(ProceedingJoinPoint joinPoint, ClearScreen screenClear) throws Throwable {
        this.terminal.puts(InfoCmp.Capability.clear_screen, new Object[0]);
        joinPoint.proceed();
        return joinPoint;
    }

}