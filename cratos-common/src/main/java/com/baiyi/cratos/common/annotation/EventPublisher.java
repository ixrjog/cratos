package com.baiyi.cratos.common.annotation;

import java.lang.annotation.*;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/7 上午11:21
 * &#064;Version 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface EventPublisher {

    String topic();

}
