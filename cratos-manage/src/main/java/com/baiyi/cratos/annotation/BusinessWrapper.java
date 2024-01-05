package com.baiyi.cratos.annotation;

import com.baiyi.cratos.domain.enums.BusinessTypeEnum;

import java.lang.annotation.*;

/**
 * @Author baiyi
 * @Date 2024/1/5 10:50
 * @Version 1.0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface BusinessWrapper {

    boolean atBefore() default false;

    BusinessTypeEnum[] businessEnums() default {};

}
