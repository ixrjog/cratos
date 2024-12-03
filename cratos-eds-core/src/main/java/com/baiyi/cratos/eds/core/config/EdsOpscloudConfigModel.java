package com.baiyi.cratos.eds.core.config;

import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.eds.core.config.base.IEdsConfigModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/2 10:38
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class EdsOpscloudConfigModel {

    @Data
    @NoArgsConstructor
    @Schema
    public static class Opscloud implements IEdsConfigModel {
        private final String version = "4";
        private String url;
        @Schema(description = "凭据")
        private Cred cred;
        private EdsInstance edsInstance;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Cred {
        private String accessToken;
    }

}
