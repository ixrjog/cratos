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
public class Cert {

    @Data
    public static class Result {
        /**
         * {
         * "result": [{
         * "id": "38d2cf87-aeea-4aad-abcc-ef9c87e56614",
         * "type": "sni_custom",
         * "hosts": ["*.palmpay.app", "palmpay.app"],
         * "primary_certificate": "b532e8d5-2640-4c01-8f61-895bffbf2d7d",
         * "status": "active",
         * "certificates": [{
         * "id": "b532e8d5-2640-4c01-8f61-895bffbf2d7d",
         * "hosts": ["*.palmpay.app", "palmpay.app"],
         * "issuer": "DigiCertInc",
         * "signature": "SHA256WithRSA",
         * "status": "active",
         * "bundle_method": "ubiquitous",
         * "zone_id": "5243357f773b873952f7f99090841934",
         * "uploaded_on": "2024-01-24T03:30:01.362533Z",
         * "modified_on": "2024-01-24T03:30:03.220297Z",
         * "expires_on": "2025-02-08T23:59:59.000000Z",
         * "priority": null
         * }],
         * "created_on": "2024-01-24T03:30:01.362533Z",
         * "certificate_authority": "digicert"    * 	}],
         * "result_info": {
         * "page": 1,
         * "per_page": 20,
         * "total_pages": 1,
         * "count": 1,
         * "total_count": 1
         * },
         * "success": true,
         * "errors": [],
         * "messages": []
         * }
         */
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

        // "created_on": "2023-12-06T06:15:12.630995Z"
        @JsonProperty("expires_on")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")
        private Date expiresOn;

    }

}
