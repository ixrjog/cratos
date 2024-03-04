package com.baiyi.cratos.eds.cloudflare.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2024/3/4 13:35
 * @Version 1.0
 */
public class Zone {

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
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
        private Date modifiedOn;

        // "created_on": "2023-12-06T06:15:12.630995Z"
        @JsonProperty("created_on")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
        private Date createdOn;

        // "activated_on": "2023-12-06T06:21:28.472800Z",
        @JsonProperty("activated_on")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
        private Date activatedOn;

    }

}
