package com.baiyi.cratos.aspect;

import com.baiyi.cratos.annotation.DomainEncrypt;
import com.baiyi.cratos.domain.annotation.EncryptedDomain;
import com.baiyi.cratos.domain.annotation.FieldEncrypt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * @Author baiyi
 * @Date 2024/1/8 09:49
 * @Version 1.0
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
@Order(-1)
public class DomainEncryptAspect {

    private static final boolean ENCRYPT = true;
    private static final boolean ERASE = false;

    private final StringEncryptor stringEncryptor;

    @Pointcut(value = "@annotation(com.baiyi.cratos.annotation.DomainEncrypt)")
    public void annotationPoint() {
    }

    @Before(value = "@annotation(domainEncrypt)")
    public void beforeAdvice(JoinPoint joinPoint, DomainEncrypt domainEncrypt) {
        if (!AopUtils.getTargetClass(joinPoint.getArgs()[0])
                .isAnnotationPresent(EncryptedDomain.class)) {
            return;
        }
        operationDomain(joinPoint.getArgs()[0], ENCRYPT);
    }

    @After(value = "@annotation(domainEncrypt)")
    public void afterAdvice(JoinPoint joinPoint, DomainEncrypt domainEncrypt) {
        if (AopUtils.getTargetClass(joinPoint.getArgs()[0])
                .isAnnotationPresent(EncryptedDomain.class)) {
            return;
        }
        operationDomain(joinPoint.getArgs()[0], ERASE);
    }

    private void operationDomain(Object domain, boolean action) {
        Field[] fields = domain.getClass()
                .getDeclaredFields();
        Arrays.stream(fields)
                .filter(field -> field.isAnnotationPresent(FieldEncrypt.class))
                .forEach(field -> {
                    field.setAccessible(true);
                    if (action == ENCRYPT) {
                        try {
                            // 为空时不加密
                            String fieldValue = (String) field.get(domain);
                            if (StringUtils.hasText(fieldValue)) {
                                field.set(domain, stringEncryptor.encrypt(fieldValue));
                            }
                        } catch (IllegalAccessException e) {
                            // 忽略字段类型错误
                        }
                    } else {
                        boolean erase = field.getAnnotation(FieldEncrypt.class)
                                .erase();
                        if (erase) {
                            try {
                                field.set(domain, null);
                            } catch (IllegalAccessException e) {
                                // 忽略字段类型错误
                            }
                        }
                    }
                });
    }

}
