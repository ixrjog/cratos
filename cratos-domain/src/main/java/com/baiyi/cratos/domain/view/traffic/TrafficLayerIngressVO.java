package com.baiyi.cratos.domain.view.traffic;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/8/14 上午10:51
 * &#064;Version 1.0
 */
public class TrafficLayerIngressVO {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class IngressDetails implements Serializable {
        @Serial
        private static final long serialVersionUID = 8597024146017004032L;
        public static final IngressDetails EMPTY = IngressDetails.builder()
                .build();
        private String ingressTable;

        @Override
        public String toString() {
            return ingressTable;
        }
    }

}
