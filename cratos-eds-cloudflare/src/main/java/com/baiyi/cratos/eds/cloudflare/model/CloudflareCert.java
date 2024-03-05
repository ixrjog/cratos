package com.baiyi.cratos.eds.cloudflare.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/3/1 18:05
 * @Version 1.0
 */
public class CloudflareCert {

    @Data
    public static class Result {
        private String id;
        private String type;
        private List<String> hosts;

        @JsonProperty("primary_certificate")
        private String primaryCertificate;

        private String status;

        private List<Certificate> certificates;
        @JsonProperty("certificate_authority")
        private String certificateAuthority;

        @JsonProperty("created_on")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
        private Date createdOn;
    }

    @Data
    public static class Certificate {

        private String id;
        private List<String> hosts;
        private String issuer;
        private String signature;
        private String status;
        @JsonProperty("bundle_method")
        private String bundleMethod;

        @JsonProperty("zone_id")
        private String zoneId;

        @JsonProperty("uploaded_on")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
        private Date uploadedOn;

        @JsonProperty("modified_on")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
        private Date modifiedOn;

        @JsonProperty("expires_on")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
        private Date expiresOn;

    }

}
