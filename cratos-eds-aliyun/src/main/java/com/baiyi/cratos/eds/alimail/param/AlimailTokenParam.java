package com.baiyi.cratos.eds.alimail.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/11 14:49
 * &#064;Version 1.0
 */
public class AlimailTokenParam {

    /**
     * -d 'grant_type=client_credentials&client_id=...&client_secret=...'
     */
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class GetToken {
        @Builder.Default
        @JsonProperty("grant_type")
        private String grantType = "client_credentials";
        private String clientId;
        private String clientSecret;
    }

}
