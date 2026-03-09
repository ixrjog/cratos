package com.baiyi.cratos.eds.aliyun.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/3/6 14:30
 * &#064;Version 1.0
 */
public class AliyunOss {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BucketPolicy {
        @JsonProperty(value = "Version")
        private String version;
        @JsonProperty(value = "Statement")
        private List<Statement> statement;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Statement {
        @JsonProperty(value = "Principal")
        private List<String> principal;
        @JsonProperty(value = "Effect")
        private String effect;
        @JsonProperty(value = "Resource")
        private List<String> resource;
        @JsonProperty(value = "Action")
        private List<String> action;
    }

}
