package com.baiyi.cratos.eds.core.config.model;

import com.baiyi.cratos.eds.core.config.base.ToAuthorization;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/3/9 14:42
 * &#064;Version 1.0
 */
public class EdsSreEventBridgeConfigModel {

    @Data
    @NoArgsConstructor
    @Schema
    public static class Cred implements ToAuthorization.ToAuthorizationBearer {
        private String token;
    }

}
