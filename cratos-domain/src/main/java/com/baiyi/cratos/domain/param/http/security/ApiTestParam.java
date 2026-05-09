package com.baiyi.cratos.domain.param.http.security;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/5/7 11:09
 * &#064;Version 1.0
 */
public class ApiTestParam {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class CallApi {
        @Schema(description = "请求报文")
        private String requestMessage;
        @Schema(description = "请求源站")
        private String originServer;
        private String ppToken;
        @Schema(description = "签名算法")
        private String signatureAlgorithm;
        @Schema(description = "签名私钥")
        private String privateKeyType;
        private Boolean convertToHTTPS;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class SaveSignMap {
        @Schema(description = "YAML格式的签名映射配置")
        private String signMapYaml;
    }

}
