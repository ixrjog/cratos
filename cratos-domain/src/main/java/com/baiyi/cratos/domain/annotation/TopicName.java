package com.baiyi.cratos.domain.annotation;

import java.lang.annotation.*;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/1/22 10:46
 * &#064;Version 1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface TopicName  {

    String nameOf();

}
