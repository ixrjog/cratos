package com.baiyi.cratos.annotation;

import java.lang.annotation.*;

/**
 * 领域加密
 * @Author baiyi
 * @Date 2024/1/8 09:47
 * @Version 1.0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface DomainEncrypt {

    boolean erase() default false;

}
