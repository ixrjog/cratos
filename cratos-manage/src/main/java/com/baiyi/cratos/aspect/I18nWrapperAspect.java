package com.baiyi.cratos.aspect;

import com.baiyi.cratos.annotation.I18nWrapper;
import com.baiyi.cratos.domain.model.I18nModel;
import com.baiyi.cratos.domain.util.I18nUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/10 15:51
 * &#064;Version 1.0
 */
@Slf4j
@Aspect
@Component
public class I18nWrapperAspect {

    @Pointcut(value = "@annotation(com.baiyi.cratos.annotation.I18nWrapper)")
    public void annotationPoint() {
    }

    @After(value = "@annotation(i18nWrapper)")
    public void afterAdvice(JoinPoint joinPoint, I18nWrapper i18nWrapper) {
        try {
            Object arg0 = joinPoint.getArgs()[0];
            if (arg0 instanceof I18nModel.HasI18n hasI18n) {
                I18nUtils.setI18nData(hasI18n);
            }
        } catch (Exception ignored) {
        }
    }

}
