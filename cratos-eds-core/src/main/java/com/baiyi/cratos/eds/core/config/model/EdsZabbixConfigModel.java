package com.baiyi.cratos.eds.core.config.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/10/29 10:44
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class EdsZabbixConfigModel {

    @Data
    @NoArgsConstructor
    @Schema
    public static class Cred {
        private String username;
        private String password;
    }

    /**
     * alert:
     * silencing:
     * rules:
     * - 'Linux: High memory utilization *'
     */
    @Data
    @NoArgsConstructor
    @Schema
    public static class Alert {
        private Silencing silencing;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Silencing {
        private List<String> rules;
    }

}
