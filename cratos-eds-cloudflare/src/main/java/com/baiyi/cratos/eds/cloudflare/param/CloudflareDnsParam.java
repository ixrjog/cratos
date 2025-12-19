package com.baiyi.cratos.eds.cloudflare.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/12/19 13:42
 * &#064;Version 1.0
 */
public class CloudflareDnsParam {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateDnsRecord {

        private String name;
        private String type;
        @Builder.Default
        private Long ttl = 1L;
        private String comment;
        @Builder.Default
        private Boolean proxied = true;
        private String content;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateDnsRecord {

        private String name;
        private String type;
        @Builder.Default
        private Long ttl = 1L;
        private String comment;
        @Builder.Default
        private Boolean proxied = true;
        private String content;
    }

}
