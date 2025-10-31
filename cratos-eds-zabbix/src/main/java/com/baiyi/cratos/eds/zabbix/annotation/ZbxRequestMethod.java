package com.baiyi.cratos.eds.zabbix.annotation;

import com.baiyi.cratos.eds.zabbix.enums.ZbxAPIAction;
import com.baiyi.cratos.eds.zabbix.enums.ZbxAPIGroup;

import java.lang.annotation.*;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/10/29 13:37
 * &#064;Version 1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface ZbxRequestMethod {

    ZbxAPIGroup group();

    ZbxAPIAction action();

}