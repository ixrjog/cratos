package com.baiyi.cratos.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/6/3 16:30
 * &#064;Version 1.0
 */
public class ConfigMapModel {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class ConfigMap implements Serializable {
        @Serial
        private static final long serialVersionUID = 58972027212499180L;
        private Map<String, String> data;
    }

}
