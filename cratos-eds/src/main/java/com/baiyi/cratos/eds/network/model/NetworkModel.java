package com.baiyi.cratos.eds.network.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/4/9 16:24
 * &#064;Version 1.0
 */
public class NetworkModel {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Allocation {

        private String name;
        private String region;
        private String cidr;
        @Builder.Default
        private String type = "SUBNET";
        private String vpcId;
        private String subnetId;
        private Integer assetId;
    }

}
