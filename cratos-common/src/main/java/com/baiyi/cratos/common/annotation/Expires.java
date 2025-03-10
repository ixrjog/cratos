package com.baiyi.cratos.common.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/10 11:02
 * &#064;Version 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Expires {

    long FOREVER_VALID = 0;

    long termOfValidity() default 0;

    TimeUnit unit();

}
