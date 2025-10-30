package com.baiyi.cratos.eds.core.config;

import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.eds.core.config.base.IEdsConfigModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/10/29 10:44
 * &#064;Version 1.0
 */
public class EdsZabbixConfigModel {

    @Data
    @NoArgsConstructor
    @Schema
    public static class Zabbix implements IEdsConfigModel {
        private String version;
        private String url;
        @Schema(description = "凭据")
        private Cred cred;
        private String region;
        private EdsInstance edsInstance;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Cred {
        private String username;
        private String password;
    }

}
