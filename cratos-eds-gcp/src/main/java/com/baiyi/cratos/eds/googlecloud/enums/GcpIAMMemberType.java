package com.baiyi.cratos.eds.googlecloud.enums;

import lombok.Getter;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/3/24 10:00
 * &#064;Version 1.0
 */
@Getter
public enum GcpIAMMemberType {

    USER("user"),
    SERVICE_ACCOUNT("serviceAccount");

    private final String key;

    GcpIAMMemberType(String key) {
        this.key = key;
    }

}
