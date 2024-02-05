package com.baiyi.cratos.aspect;

import com.baiyi.cratos.annotation.DomainDecrypt;
import com.baiyi.cratos.domain.annotation.EncryptedDomain;
import com.baiyi.cratos.domain.annotation.FieldEncrypt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
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
 * @Date 2024/1/10 16:13
 * @Version 1.0
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
@Order(-1)
public class DomainDecryptAspect {

    private final StringEncryptor stringEncryptor;

    @Pointcut(value = "@annotation(com.baiyi.cratos.annotation.DomainDecrypt)")
    public void annotationPoint() {
    }

    @AfterReturning(value = "@annotation(domainDecrypt)", returning = "domain")
    public void afterAdvice(JoinPoint joinPoint, DomainDecrypt domainDecrypt, Object domain) {
        if (domain == null || !AopUtils.getTargetClass(domain)
                .isAnnotationPresent(EncryptedDomain.class)) {
            return;
        }
        operationDomain(domain);
    }

    private void operationDomain(Object domain) {
        Field[] fields = domain.getClass()
                .getDeclaredFields();
        Arrays.stream(fields)
                .filter(field -> field.isAnnotationPresent(FieldEncrypt.class))
                .forEach(field -> {
                    field.setAccessible(true);
                    try {
                        String ciphertext = (String) field.get(domain);
                        if (StringUtils.hasText(ciphertext)) {
                            field.set(domain, stringEncryptor.decrypt(ciphertext));
                        }
                    } catch (IllegalAccessException e) {
                        // 忽略字段类型错误
                    }
                });
    }

}
