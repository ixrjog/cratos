package com.baiyi.cratos.eds.core.annotation;

import java.lang.annotation.*;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/3/19 18:11
 * &#064;Version 1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface CustomAsset {
    // 自定义资产
}