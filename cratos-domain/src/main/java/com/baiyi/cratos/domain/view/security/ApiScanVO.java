package com.baiyi.cratos.domain.view.security;

import com.baiyi.cratos.domain.YamlUtils;
import com.google.gson.JsonSyntaxException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/6/5 14:18
 * &#064;Version 1.0
 */
public class ApiScanVO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class ScanConfig implements Serializable {

        public static ApiScanVO.ScanConfig loadAs(String content) {
            if (StringUtils.isBlank(content)) {
                return null;
            }
            try {
                return YamlUtils.loadAs(content, ScanConfig.class);
            } catch (JsonSyntaxException e) {
                throw new RuntimeException("API scan config format error: {}");
            }
        }

        @Serial
        private static final long serialVersionUID = 3120626260086280477L;
        private List<ScanGroup> scanConfig;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class ScanGroup implements Serializable {
        @Serial
        private static final long serialVersionUID = 4120626260086280478L;
        private String name;
        private String severity;
        private String category;
        private List<ScanRule> rules;
        private ScanExpect expect;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class ScanRule implements Serializable {
        @Serial
        private static final long serialVersionUID = 5120626260086280479L;
        private String path;
        private List<String> methods;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class ScanExpect implements Serializable {
        @Serial
        private static final long serialVersionUID = 6120626260086280480L;
        private List<Integer> statusNot;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class ScanResult implements Serializable {
        @Serial
        private static final long serialVersionUID = 7120626260086280481L;
        private String appName;
        private String deploymentName;
        private String podIP;
        private String path;
        private String method;
        private int statusCode;
        private String resp;
        private int respSize;
        private String groupName;
        private String severity;
        private String category;
        private String scanBatch;
    }

}
