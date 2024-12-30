package com.baiyi.cratos.common.enums;

import lombok.Getter;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/30 15:30
 * &#064;Version 1.0
 */
@Getter
public enum PPFramework {

    PP_JV_SPRINGBOOT_2("PPJvSpringboot2"),
    PP_JV_1("PPJv1"),
    PP_JV_2("PPJv2");

    private final String displayName;

    PPFramework (String displayName) {
        this.displayName = displayName;
    }

}
