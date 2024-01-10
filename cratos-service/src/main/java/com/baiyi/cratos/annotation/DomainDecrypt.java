package com.baiyi.cratos.annotation;

import java.lang.annotation.*;

/**
 * @Author baiyi
 * @Date 2024/1/10 16:12
 * @Version 1.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface DomainDecrypt {
}
