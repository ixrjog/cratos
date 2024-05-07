package com.baiyi.cratos.domain.annotation;

import java.lang.annotation.*;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/7 上午11:01
 * &#064;Version 1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Topic {

    String name();

}
