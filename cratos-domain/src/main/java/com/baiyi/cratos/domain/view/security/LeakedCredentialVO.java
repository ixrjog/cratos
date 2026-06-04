package com.baiyi.cratos.domain.view.security;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/5/21 14:59
 * &#064;Version 1.0
 */
public class LeakedCredentialVO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class Credentials implements Serializable {

        public static final Credentials SAFE = Credentials.builder()
                .leaked(false)
                .build();

        public static Credentials of(GcpApiKey gcpApiKey) {
            return Credentials.builder()
                    .gcpApiKey(gcpApiKey)
                    .build();
        }

        public static Credentials of(AccessKey accessKey) {
            return Credentials.builder()
                    .accessKeys(List.of(accessKey))
                    .build();
        }

        public static Credentials of(List<AccessKey> accessKeys) {
            return Credentials.builder()
                    .accessKeys(accessKeys)
                    .build();
        }

        @Serial
        private static final long serialVersionUID = 7196574440688777516L;
        @Builder.Default
        private boolean leaked = true;
        private GcpApiKey gcpApiKey;
        private List<AccessKey> accessKeys;
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
        private EdsAsset asset;
        private Map<String, EdsAssetIndex> indexMap;
    }

}
