package com.baiyi.cratos.domain.view.traffic;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/9/26 13:38
 * &#064;Version 1.0
 */
public class TrafficLayerServiceVO {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class IngressDetails implements Serializable {
        @Serial
        private static final long serialVersionUID = 8597024146017004032L;
        public static final TrafficLayerIngressVO.IngressDetails EMPTY = TrafficLayerIngressVO.IngressDetails.builder()
                .build();
        private String ingressTable;
        private Set<String> names;

        @Override
        public String toString() {
            return ingressTable;
        }
    }


}
