package com.baiyi.cratos.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/8 15:36
 * &#064;Version 1.0
 */
public class Report {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class BaseData implements Serializable {
        @Serial
        private static final long serialVersionUID = 8549723857989578574L;
        private String cName;
        private Integer value;
        private String color;
    }

}
