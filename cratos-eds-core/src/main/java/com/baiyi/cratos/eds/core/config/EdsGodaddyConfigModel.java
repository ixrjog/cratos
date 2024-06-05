package com.baiyi.cratos.eds.core.config;

import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.eds.core.config.base.IEdsConfigModel;
import com.baiyi.cratos.eds.core.config.base.ToAuthorization;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/6/5 上午11:48
 * &#064;Version 1.0
 */
public class EdsGodaddyConfigModel {

    @Data
    @NoArgsConstructor
    @Schema(description = "godaddy.com")
    public static class Gandi implements IEdsConfigModel {

        @Schema(description = "凭据")
        private Cred cred;

        private EdsInstance edsInstance;

    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Cred implements ToAuthorization.ToAuthorizationSsoKey {

        @Schema(description = "Authorization: sso-key <Key:Secret>")
        private String key;

        @Schema(description = "Authorization: sso-key <Key:Secret>")
        private String secret;

    }

}
