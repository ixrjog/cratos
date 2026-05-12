package com.baiyi.cratos.domain.param.http.security;

import com.baiyi.cratos.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

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

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class RecordPageQuery extends PageParam {
        @Schema(description = "查询域名")
        private String queryName;
        @Schema(description = "用户名")
        private String username;
    }

}
