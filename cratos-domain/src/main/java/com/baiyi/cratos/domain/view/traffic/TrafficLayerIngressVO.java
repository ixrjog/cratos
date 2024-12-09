package com.baiyi.cratos.domain.view.traffic;

import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.google.common.collect.Lists;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

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
        private Set<String> names;

        @Override
        public String toString() {
            return ingressTable;
        }
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class IngressTrafficLimit implements Serializable {
        @Serial
        private static final long serialVersionUID = 5026762603991816532L;
        private EdsAssetVO.Asset asset;
        @Builder.Default
        private List<EdsAssetIndex> rules = Lists.newArrayList();
        private EdsAssetIndex namespace;
        private EdsAssetIndex loadBalancer;
        private EdsAssetIndex trafficLimitQps;
        private EdsAssetIndex sourceIp;
    }

}
