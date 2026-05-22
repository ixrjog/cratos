package com.baiyi.cratos.domain.view.security;

import com.baiyi.cratos.domain.generator.EdsAsset;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/5/21 14:59
 * &#064;Version 1.0
 */
public class LeakedCredential {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class Credentials implements Serializable {
        @Serial
        private static final long serialVersionUID = 7196574440688777516L;
        private GcpApiKey gcpAPIKey;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class GcpApiKey implements Serializable {
        @Serial
        private static final long serialVersionUID = -1487781580819364726L;
        private String instanceName;
        private EdsAsset asset;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class AccessKey implements Serializable {
        @Serial
        private static final long serialVersionUID = 2301693351343660771L;
        private String instanceName;


    }


}
