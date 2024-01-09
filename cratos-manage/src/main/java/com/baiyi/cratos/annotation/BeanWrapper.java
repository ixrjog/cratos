package com.baiyi.cratos.annotation;

import java.lang.annotation.*;

/**
 * @Author baiyi
 * @Date 2024/1/9 13:53
 * @Version 1.0
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface BeanWrapper {
}
