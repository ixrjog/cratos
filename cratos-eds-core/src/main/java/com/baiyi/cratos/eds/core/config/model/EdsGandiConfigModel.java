package com.baiyi.cratos.eds.core.config.model;

import com.baiyi.cratos.eds.core.config.base.ToAuthorization;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/6/4 下午3:15
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class EdsGandiConfigModel {

    @Data
    @NoArgsConstructor
    @Schema
    public static class Cred implements ToAuthorization.ToAuthorizationBearer, ToAuthorization.ToAuthorizationApikey {
        @Schema(description = "Authorization: Bearer <AccessToken>")
        private String accessToken;
        @Schema(description = "Authorization: Apikey <Apikey>")
        private String apikey;

        @Override
        public String getToken() {
            return accessToken;
        }
    }

}
