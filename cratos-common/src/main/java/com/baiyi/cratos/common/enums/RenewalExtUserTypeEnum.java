package com.baiyi.cratos.common.enums;

import lombok.Getter;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/6 10:36
 * &#064;Version 1.0
 */
@Getter
public enum RenewalExtUserTypeEnum {

    SHORT_TERM("short-term", 7L),
    MID_TERM("mid-term", 30L),
    LONG_TERM("long-term", 90L);

    private final String displayName;
    private final long days;

    RenewalExtUserTypeEnum(String displayName, long days) {
        this.displayName = displayName;
        this.days = days;
    }

}
