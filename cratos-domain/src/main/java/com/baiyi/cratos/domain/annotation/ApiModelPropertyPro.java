package com.baiyi.cratos.domain.annotation;

import java.lang.annotation.*;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/8/5 下午4:04
 * &#064;Version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiModelPropertyPro {

    Class<? extends Enum<?>> value();

    String method() default "name";

}
