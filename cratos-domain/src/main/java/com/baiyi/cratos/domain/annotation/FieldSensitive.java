package com.baiyi.cratos.domain.annotation;

import com.baiyi.cratos.domain.enums.SensitiveType;

import java.lang.annotation.*;

/**
 * @Author baiyi
 * @Date 2024/1/8 11:21
 * @Version 1.0
 */
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FieldSensitive {

    SensitiveType type() default SensitiveType.CUSTOMER;

    int prefixNoMaskLen() default 0;

    int suffixNoMaskLen() default 0;

    String symbol() default "*";

}
