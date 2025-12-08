package com.baiyi.cratos.eds.cloudflare.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/6/9 09:42
 * &#064;Version 1.0
 */
public class CloudflareDns {

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DnsRecord {
        private String id;
        private String zoneId;
        private String name;
        private String content;
        private String comment;
        private Boolean proxied;
        private Boolean proxiable;
        private List<String> tags;
        private Integer ttl;
        private String type;
    }

}
