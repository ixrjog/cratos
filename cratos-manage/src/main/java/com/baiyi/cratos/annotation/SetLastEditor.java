package com.baiyi.cratos.annotation;

import java.lang.annotation.*;

/**
 * @Author baiyi
 * @Date 2024/2/23 10:45
 * @Version 1.0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface SetLastEditor  {

    String desc() default "";

}
