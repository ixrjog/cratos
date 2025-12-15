package com.baiyi.cratos.eds.core.config;

import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.eds.core.config.base.IEdsConfigModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/11/10 17:54
 * &#064;Version 1.0
 */
public class EdsAzureConfigModel {

    @Data
    @NoArgsConstructor
    @Schema
    public static class Azure implements IEdsConfigModel {
        private Cred cred;
        private Login login;
        private EdsInstance edsInstance;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Cred {
        private String tenantId;
        // private String authority;
        private String clientId;
        private String secretId;
        private String secretValue;
        private String scope;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Login {
        private String domain;
        private String loginUrl;
    }

}
