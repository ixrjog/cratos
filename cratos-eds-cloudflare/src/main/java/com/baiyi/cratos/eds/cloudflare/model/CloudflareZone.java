package com.baiyi.cratos.eds.cloudflare.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

import static com.baiyi.cratos.domain.constant.Global.ISO8601_S6;
import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/3/4 13:35
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class CloudflareZone {

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Result {

        @Schema(description = "zoneId")
        private String id;

        private String name;

        private String status;

        private Boolean paused;

        private String type;

        @JsonProperty("development_mode")
        private Integer developmentMode;

        @JsonProperty("verification_key")
        private String verificationKey;

        @JsonProperty("cname_suffix")
        private String cnameSuffix;

        // "modified_on": "2023-12-06T06:21:51.455654Z"
        @JsonProperty("modified_on")
        @JsonFormat(pattern = ISO8601_S6)
        private Date modifiedOn;

        // "created_on": "2023-12-06T06:15:12.630995Z"
        @JsonProperty("created_on")
        @JsonFormat(pattern = ISO8601_S6)
        private Date createdOn;

        // "activated_on": "2023-12-06T06:21:28.472800Z",
        @JsonProperty("activated_on")
        @JsonFormat(pattern = ISO8601_S6)
        private Date activatedOn;

    }

}
