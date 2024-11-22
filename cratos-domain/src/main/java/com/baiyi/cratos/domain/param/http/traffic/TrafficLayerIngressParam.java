package com.baiyi.cratos.domain.param.http.traffic;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/8/14 下午1:56
 * &#064;Version 1.0
 */
public class TrafficLayerIngressParam {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class QueryIngressHostDetails {
        @NotBlank
        private String queryHost;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class QueryIngressDetails {
        @NotBlank
        private String name;
    }

}
