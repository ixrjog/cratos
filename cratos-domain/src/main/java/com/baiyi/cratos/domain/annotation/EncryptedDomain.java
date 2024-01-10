package com.baiyi.cratos.domain.annotation;

import java.lang.annotation.*;

/**
 * @Author baiyi
 * @Date 2024/1/10 16:47
 * @Version 1.0
 */
@Target({ElementType.FIELD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EncryptedDomain {
}
