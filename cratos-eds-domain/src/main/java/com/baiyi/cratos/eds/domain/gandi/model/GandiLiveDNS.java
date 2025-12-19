package com.baiyi.cratos.eds.domain.gandi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/12/19 16:29
 * &#064;Version 1.0
 */
public class GandiLiveDNS {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Record implements Serializable {
        @Serial
        private static final long serialVersionUID = -8980064915410737332L;
        @JsonProperty("rrset_name")
        private String rrsetName;
        @JsonProperty("rrset_ttl")
        private Long rrsetTtl;
        @JsonProperty("rrset_type")
        private String rrsetType;
        @JsonProperty("rrset_values")
        private List<String> rrsetValues;
        @JsonProperty("rrset_href")
        private String rrsetHref;
    }

}
