package com.baiyi.cratos.eds.zabbix.enums;

import lombok.Getter;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/11/4 16:26
 * &#064;Version 1.0
 */
@Getter
public enum HostStatus {

    ENABLED(0),
    DISABLED(1);

    private final int code;

    HostStatus(int code) {
        this.code = code;
    }

}
