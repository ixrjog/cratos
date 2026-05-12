package com.baiyi.cratos.domain.view.security;

import com.baiyi.cratos.domain.constant.Global;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/5/11 15:17
 * &#064;Version 1.0
 */
public class ApiSecurityTestVO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class Record implements Serializable {
        @Serial
        private static final long serialVersionUID = 3955570305905437859L;
        private Integer id;
        private String username;
        private String requestUrl;
        private String requestMethod;
        private String requestHost;
        private String originServer;
        private String signatureAlgorithm;
        private String privateKeyType;
        private String requestHeaders;
        private String requestBody;
        private Integer responseStatus;
        private String responseHeaders;
        private String responseBody;
        private Long elapsedMs;
        private Boolean success;
        private String comment;
        @JsonFormat(timezone = "UTC", pattern = Global.ISO8601)
        private Date createTime;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class RecordSummary implements Serializable {
        @Serial
        private static final long serialVersionUID = 9098886853521908231L;
        private Integer id;
        private String username;
        private String requestUrl;
        private String requestMethod;
        private String signatureAlgorithm;
        private String privateKeyType;
        private String requestHeaders;
        private String requestBody;
        private Integer responseStatus;
        private String responseHeaders;
        private String responseBody;
        private Long elapsedMs;
        private Boolean success;
        @JsonFormat(timezone = "UTC", pattern = Global.ISO8601)
        private Date createTime;
    }

}
