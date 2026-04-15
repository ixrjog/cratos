package com.baiyi.cratos.eds.core.config.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/4/15 14:13
 * &#064;Version 1.0
 */
public class EdsApiRiskConfigModel {

    @Data
    @NoArgsConstructor
    @Schema
    public static class Cred {
        private String accessKey;
        private String secretKey;
    }

}
