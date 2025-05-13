package com.baiyi.cratos.eds.core.annotation;

import java.lang.annotation.*;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/5/13 09:59
 * &#064;Version 1.0
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AssetField {

    String desc() default "";

    String aliasName();

    boolean required() default true;

}
