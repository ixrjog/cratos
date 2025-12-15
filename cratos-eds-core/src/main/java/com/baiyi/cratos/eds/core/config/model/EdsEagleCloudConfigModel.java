package com.baiyi.cratos.eds.core.config.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/9/29 15:20
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class EdsEagleCloudConfigModel {

    @Data
    @NoArgsConstructor
    @Schema
    public static class Cred {
        private String token;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class SecurityAdministrator {
        private String name;
        private String dingtalkUserId;
    }

}
