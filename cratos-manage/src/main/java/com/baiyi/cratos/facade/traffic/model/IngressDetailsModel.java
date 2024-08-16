package com.baiyi.cratos.facade.traffic.model;

import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.google.api.client.util.Lists;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/8/16 上午10:42
 * &#064;Version 1.0
 */
public class IngressDetailsModel {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class IngressEntry implements Comparable<IngressEntry> {
        private String kubernetes;
        private String ingress;
        private String rule;
        private String service;
        private String lb;

        @Override
        public int compareTo(@NonNull IngressEntry o) {
            return service.compareTo(o.getService());
        }
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class IngressIndexDetails {
        private EdsAssetIndex namespace;
        @Schema(description = "loadBalancer.ingress.hostname")
        private EdsAssetIndex hostname;
        @Builder.Default
        private List<EdsAssetIndex> rules = Lists.newArrayList();
    }

}
