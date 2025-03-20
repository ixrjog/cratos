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

    boolean BEFORE = true;
    boolean AFTER = false;

    boolean invokeAt() default AFTER;

    /**
     * 不指定，则从类注解@BusinessType中获取类型
     * @return
     */
    BusinessTypeEnum[] ofTypes() default {};

}
