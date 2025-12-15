package com.baiyi.cratos.eds.core.config.model;

import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.eds.core.config.base.IEdsConfigModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/11 13:44
 * &#064;Version 1.0
 */
public class EdsAlimailConfigModel {

    @Data
    @NoArgsConstructor
    @Schema
    public static class Alimail implements IEdsConfigModel {
        private String version;
        @Schema(description = "凭据")
        private Cred cred;
        private Api api;
        private String loginUrl;
        private EdsInstance edsInstance;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Cred {
        @Schema(description = "应用ID")
        private String id;
        private String secret;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Api {
        private String url;
    }

}
