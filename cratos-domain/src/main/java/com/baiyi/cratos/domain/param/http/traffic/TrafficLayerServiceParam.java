package com.baiyi.cratos.domain.param.http.traffic;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/9/26 13:36
 * &#064;Version 1.0
 */
public class TrafficLayerServiceParam {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class QueryServiceIngress {
        @NotBlank
        private String name;
    }

}
