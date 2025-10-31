package com.baiyi.cratos.eds.zabbix.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/10/30 11:01
 * &#064;Version 1.0
 */
public class ZbxCommonResult {

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Tag implements Serializable {
        @Serial
        private static final long serialVersionUID = -3088056730800718192L;
        private String tag;
        private String value;
    }

}
