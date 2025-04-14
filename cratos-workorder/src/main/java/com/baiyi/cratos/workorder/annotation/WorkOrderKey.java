package com.baiyi.cratos.workorder.annotation;

import com.baiyi.cratos.workorder.enums.WorkOrderKeys;

import java.lang.annotation.*;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/14 10:08
 * &#064;Version 1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface WorkOrderKey {

    WorkOrderKeys key();

}