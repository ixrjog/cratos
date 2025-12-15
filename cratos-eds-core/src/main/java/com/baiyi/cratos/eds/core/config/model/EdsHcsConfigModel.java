package com.baiyi.cratos.eds.core.config.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/7/28 14:35
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class EdsHcsConfigModel {

    @Data
    @NoArgsConstructor
    @Schema
    public static class Cred {
        private String username;
        private String accessKey;
        private String secretKey;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class ManageOne {
        private String consoleUrl;
    }

}
