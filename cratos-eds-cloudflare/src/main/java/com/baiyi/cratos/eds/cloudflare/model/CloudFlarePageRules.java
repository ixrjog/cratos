package com.baiyi.cratos.eds.cloudflare.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

import static com.baiyi.cratos.domain.constant.Global.ISO8601_S6;
import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/6/16 09:35
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class CloudFlarePageRules {

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PageRule {
        @Schema(description = "zoneId")
        private String id;
        private List<Action> actions;
        @JsonProperty("created_on")
        @JsonFormat(pattern = ISO8601_S6)
        private Date createdOn;
        @JsonProperty("modified_on")
        @JsonFormat(pattern = ISO8601_S6)
        private Date modifiedOn;
        private Integer priority;
        private String status;
        private List<Target> targets;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Action {
        private String id;
        private String value;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Target {
        private Constraint constraint;
        private String target;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Constraint {
        private String operator;
        private String value;
    }

}
