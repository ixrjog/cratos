package com.baiyi.cratos.annotation;

import java.lang.annotation.*;

/**
 * @Author baiyi
 * @Date 2024/1/8 11:43
 * @Version 1.0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Sensitive {

    // 脱敏切面

}
