package com.baiyi.cratos.common.enums;

import lombok.Getter;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/4/3 16:31
 * &#064;Version 1.0
 */
@Getter
public enum ApiSecurityProgressEnum {

    PENDING("待确认"),
    CONFIRMING("确认中"),
    CONFIRMED("已确认"),
    FIXED("已修复");

    private final String desc;

    ApiSecurityProgressEnum(String desc) {
        this.desc = desc;
    }

}
