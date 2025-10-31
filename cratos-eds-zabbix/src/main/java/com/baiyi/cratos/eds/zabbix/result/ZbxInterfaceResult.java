package com.baiyi.cratos.eds.zabbix.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/10/30 16:20
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ZbxInterfaceResult {

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Interface implements Serializable {
        @Serial
        private static final long serialVersionUID = 1654500039678161772L;
        private String interfaceid;
        private String hostid;
        private Integer main;
        private Integer type;
        private Integer useip;
        private String ip;
        private String dns;
        private Integer port;
        private Integer available;
    }

}
