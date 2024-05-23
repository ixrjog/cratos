package com.baiyi.cratos.annotation;

import com.baiyi.cratos.domain.enums.BusinessTypeEnum;

import java.lang.annotation.*;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/23 上午10:22
 * &#064;Version 1.0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface PageQueryByTag {

    BusinessTypeEnum ofType();

}
