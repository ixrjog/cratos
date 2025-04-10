package com.baiyi.cratos.annotation;

import java.lang.annotation.*;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/10 15:50
 * &#064;Version 1.0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface I18nWrapper {

}
