package com.baiyi.cratos.eds.domain.model;

import com.baiyi.cratos.domain.constant.Global;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/6/4 下午2:56
 * &#064;Version 1.0
 */
public class GandiDomain {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Domain implements Serializable {

        @Serial
        private static final long serialVersionUID = 443132222371584651L;

        private List<String> status;

        private Dates dates;

        @JsonIgnore
        private List<String> tags;

        private String fqdn;

        private String id;

        private Boolean autorenew;

        private String tld;

        private String owner;

        @JsonProperty("orga_owner")
        private String orgaOwner;

        @JsonProperty("domain_owner")
        private String domainOwner;

        private Nameserver nameserver;

        private String href;

        @JsonProperty("fqdn_unicode")
        private String fqdnUnicode;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Dates implements Serializable {

        @Serial
        private static final long serialVersionUID = 7247456382156793010L;

        @JsonProperty("created_at")
        @JsonFormat(pattern = Global.ISO8601)
        private Date createdAt;

        @JsonProperty("registry_created_at")
        @JsonFormat(pattern = Global.ISO8601)
        private Date registryCreatedAt;

        @JsonProperty("registry_ends_at")
        @JsonFormat(pattern = Global.ISO8601)
        private Date registryEndsAt;

        @JsonProperty("updated_at")
        @JsonFormat(pattern = Global.ISO8601)
        private Date updatedAt;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Nameserver implements Serializable {

        @Serial
        private static final long serialVersionUID = 6425176106906973148L;

        private String current;
    }

}