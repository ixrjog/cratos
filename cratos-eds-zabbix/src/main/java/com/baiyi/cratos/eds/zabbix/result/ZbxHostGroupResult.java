package com.baiyi.cratos.eds.zabbix.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/10/30 10:00
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ZbxHostGroupResult {

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class HostGroup implements Serializable {
        @Serial
        private static final long serialVersionUID = -8636878208166795194L;
        private String groupid;
        private String name;
        /**
         * 主机组的来源。
         * 0 - 普通的主机组;
         * 4 - 被发现的主机组。
         */
        private Integer flags;
        /**
         * 无论该组是否由系统内部使用，内部组无法被删除。
         * 0 - (默认) 不是内部；
         * 1 - 内部。
         */
        private Integer internal;
    }

}
