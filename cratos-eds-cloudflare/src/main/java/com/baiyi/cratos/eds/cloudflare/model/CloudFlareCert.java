package com.baiyi.cratos.eds.cloudflare.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

import static com.baiyi.cratos.domain.constant.Global.ISO8601_S6;
import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/3/1 18:05
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class CloudFlareCert {

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
        @JsonFormat(pattern = ISO8601_S6)
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
        @JsonFormat(pattern = ISO8601_S6)
        private Date uploadedOn;
        @JsonProperty("modified_on")
        @JsonFormat(pattern = ISO8601_S6)
        private Date modifiedOn;
        @JsonProperty("expires_on")
        @JsonFormat(pattern = ISO8601_S6)
        private Date expiresOn;
    }

    @Data
    public static class CustomCertificate {
        private String id;
        @JsonProperty("zone_id")
        private String zoneId;
        @JsonProperty("bundle_method")
        private String bundleMethod;
        @JsonProperty("expires_on")
        private String expiresOn;
        @JsonProperty("geo_restrictions")
        private GeoRestrictions geoRestrictions;
        private List<String> hosts;
        private String issuer;
        @JsonProperty("keyless_server")
        private KeylessServer keylessServer;
        @JsonProperty("modified_on")
        private String modifiedOn;
        @JsonProperty("policy_restrictions")
        private String policyRestrictions;
        private Integer priority;
        private String signature;
        private String status;
        @JsonProperty("uploaded_on")
        private String uploadedOn;
    }

    @Data
    public static class GeoRestrictions {
        private String label;
    }

    @Data
    public static class KeylessServer {
        private String id;
        @JsonProperty("created_on")
        private String createdOn;
        private Boolean enabled;
        private String host;
        @JsonProperty("modified_on")
        private String modifiedOn;
        private String name;
        private List<String> permissions;
        private Integer port;
        private String status;
        private Tunnel tunnel;
    }

    @Data
    public static class Tunnel {
        @JsonProperty("private_ip")
        private String privateIp;
        @JsonProperty("vnet_id")
        private String vnetId;
    }

}
