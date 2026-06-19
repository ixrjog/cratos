package com.baiyi.cratos.domain.param.http.traffic;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/6/16 18:41
 * &#064;Version 1.0
 */
public class TrafficLayerTopologyParam {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class QueryTrafficLayerTopology {
        private String appName;
        private String namespace;
    }

}
