package com.baiyi.cratos.annotation;

import java.lang.annotation.*;

/**
 * @Author baiyi
 * @Date 2024/2/23 09:52
 * @Version 1.0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface SetSessionUserToParam {

    String desc() default "";

}
