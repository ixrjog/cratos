package com.baiyi.cratos.eds.security.apirisk.test.model;

import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/5/6 14:12
 * &#064;Version 1.0
 */
public class GenericCall {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private String method;
        private String url;
        private Map<String, String> headers;
        private Map<String, Object> body;
        private String bodyStr;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private int statusCode;
        private Map<String, String> headers;
        @JsonRawValue
        private String body;
    }

}
