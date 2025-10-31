package com.baiyi.cratos.eds.zabbix.result;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/10/29 17:43
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ZbxTemplateResult {

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Template implements Serializable {
        @Serial
        private static final long serialVersionUID = -1988958009422853206L;
        private String templateid;
        private String name;
        // 模板的正式名称。
        private String host;
        private String description;
    }

}
