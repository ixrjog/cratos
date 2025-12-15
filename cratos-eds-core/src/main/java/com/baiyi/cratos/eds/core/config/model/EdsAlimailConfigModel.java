package com.baiyi.cratos.eds.core.config.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/11 13:44
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class EdsAlimailConfigModel {

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
