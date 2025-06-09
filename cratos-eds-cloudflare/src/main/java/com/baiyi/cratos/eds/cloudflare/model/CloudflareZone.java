package com.baiyi.cratos.eds.cloudflare.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    public static class Zone {
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
    }

}
