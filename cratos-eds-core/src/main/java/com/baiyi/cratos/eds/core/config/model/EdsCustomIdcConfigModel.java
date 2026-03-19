package com.baiyi.cratos.eds.core.config.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/3/19 15:43
 * &#064;Version 1.0
 */
public class EdsCustomIdcConfigModel {

    @Data
    @NoArgsConstructor
    @Schema
    public static class IDCInfo  {
        // ISO 3166-1 alpha-2, e.g. "NG", "KE"
        private String countryCode;
        // ITU-T E.164, e.g. "+234", "+254"
        private String dialingCode;
        // e.g. "Lagos", "Nairobi"
        private String city;
        // e.g. "MainOne MDXi", "Equinix LG1"
        private String facility;
        // e.g. "Africa/Lagos"
        private String timezone;
        // 机房位置
        private Location location;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class HostConfig {
        // 主机前缀
        private String prefix;
        // 默认 region
        private String region;
        // 默认 zone
        private String zone;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Location {
        // 地址
        private String address;
        // GPS coordinates, e.g. "6.4541,3.3947"
        private String gpsCoordinates;
    }

}
