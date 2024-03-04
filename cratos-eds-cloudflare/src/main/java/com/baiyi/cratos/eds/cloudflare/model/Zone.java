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

    /**
     * {
     * "result": [{
     * "id": "7319c83281c3e94aa2d5a087e08ed3ab",
     * "name": "fleximfi.com",
     * "status": "active",
     * "paused": false,
     * "type": "partial",
     * "development_mode": 0,
     * "verification_key": "122963251-834969601",
     * "cname_suffix": "cdn.cloudflare.net",
     * "original_name_servers": ["vip3.alidns.com", "vip4.alidns.com"],
     * "original_registrar": "alibaba cloud computing ltd. d (id: 1599)",
     * "original_dnshost": null,
     * "modified_on": "2023-12-27T02:41:47.135289Z",
     * "created_on": "2023-12-27T02:35:31.891222Z",
     * "activated_on": "2023-12-27T02:41:47.135289Z",
     * "meta": {
     * "step": 2,
     * "custom_certificate_quota": 1,
     * "page_rule_quota": 100,
     * "phishing_detected": false,
     * "multiple_railguns_allowed": false
     * },
     * "owner": {
     * "id": "94b62b3fc8103ec370e68661bcf66f41",
     * "type": "organization",
     * "name": "chuanyi——Palmpay Digital Technology"
     * },
     * "account": {
     * "id": "94b62b3fc8103ec370e68661bcf66f41",
     * "name": "chuanyi——Palmpay Digital Technology"
     * },
     * "tenant": {
     * "id": null,
     * "name": null
     * },
     * "tenant_unit": {
     * "id": null
     * },
     * "permissions": ["#waf:read", "#zone_settings:read", "#page_shield:read", "#dns_records:read", "#zone_versioning:read", "#api_gateway:read", "#zaraz:read", "#zone:read", "#web3:read", "#healthchecks:read", "#waitingroom:read", "#access:read", "#ssl:read", "#worker:read", "#logs:read", "#lb:read", "#analytics:read"],
     * "plan": {
     * "id": "94f3b7b768b0458b56d2cac4fe5ec0f9",
     * "name": "Enterprise Website",
     * "price": 0,
     * "currency": "USD",
     * "frequency": "",
     * "is_subscribed": true,
     * "can_subscribe": true,
     * "legacy_id": "enterprise",
     * "legacy_discount": false,
     * "externally_managed": true
     * }    * 	}, {
     * "id": "a590a869a48a92fbc4fc5805fb2e5140",
     * "name": "palmmerchant.com",
     * "status": "active",
     * "paused": false,
     * "type": "partial",
     * "development_mode": 0,
     * "verification_key": "221121671-800826715",
     * "cname_suffix": "cdn.cloudflare.net",
     * "original_name_servers": ["ns-1059.awsdns-04.org", "ns-140.awsdns-17.com", "ns-1898.awsdns-45.co.uk", "ns-744.awsdns-29.net"],
     * "original_registrar": "amazon registrar, inc. (id: 468)",
     * "original_dnshost": null,
     * "modified_on": "2023-11-06T09:16:01.880599Z",
     * "created_on": "2023-11-06T08:29:01.082630Z",
     * "activated_on": "2023-11-06T09:16:01.880599Z",
     * "meta": {
     * "step": 2,
     * "custom_certificate_quota": 1,
     * "page_rule_quota": 100,
     * "phishing_detected": false,
     * "multiple_railguns_allowed": false
     * }        ,
     * "owner": {
     * "id": "94b62b3fc8103ec370e68661bcf66f41",
     * "type": "organization",
     * "name": "chuanyi——Palmpay Digital Technology"
     * },
     * "account": {
     * "id": "94b62b3fc8103ec370e68661bcf66f41",
     * "name": "chuanyi——Palmpay Digital Technology"
     * },
     * "tenant": {
     * "id": null,
     * "name": null
     * },
     * "tenant_unit": {
     * "id": null
     * },
     * "permissions": ["#waf:read", "#zone_settings:read", "#page_shield:read", "#dns_records:read", "#zone_versioning:read", "#api_gateway:read", "#zaraz:read", "#zone:read", "#web3:read", "#healthchecks:read", "#waitingroom:read", "#access:read", "#ssl:read", "#worker:read", "#logs:read", "#lb:read", "#analytics:read"],
     * "plan": {
     * "id": "94f3b7b768b0458b56d2cac4fe5ec0f9",
     * "name": "Enterprise Website",
     * "price": 0,
     * "currency": "USD",
     * "frequency": "",
     * "is_subscribed": true,
     * "can_subscribe": true,
     * "legacy_id": "enterprise",
     * "legacy_discount": false,
     * "externally_managed": true
     * }
     * }, {
     * "id": "5243357f773b873952f7f99090841934",
     * "name": "palmpay.app",
     * "status": "active",
     * "paused": false,
     * "type": "partial",
     * "development_mode": 0,
     * "verification_key": "349176645-816367087",
     * "cname_suffix": "cdn.cloudflare.net",
     * "original_name_servers": ["ns-199-a.gandi.net", "ns-200-b.gandi.net", "ns-34-c.gandi.net"],
     * "original_registrar": "gandi sas (id: 81)",
     * "original_dnshost": null,
     * "modified_on": "2024-01-24T03:29:37.183275Z",
     * "created_on": "2023-11-27T02:05:55.142341Z",
     * "activated_on": "2023-11-27T03:23:21.366567Z",
     * "meta": {
     * "step": 2,
     * "custom_certificate_quota": 1,
     * "page_rule_quota": 100,
     * "phishing_detected": false,
     * "multiple_railguns_allowed": false
     * },
     * "owner": {
     * "id": "94b62b3fc8103ec370e68661bcf66f41",
     * "type": "organization",
     * "name": "chuanyi——Palmpay Digital Technology"
     * },
     * "account": {
     * "id": "94b62b3fc8103ec370e68661bcf66f41",
     * "name": "chuanyi——Palmpay Digital Technology"
     * },
     * "tenant": {
     * "id": null,
     * "name": null
     * },
     * "tenant_unit": {
     * "id": null
     * },
     * "permissions": ["#waf:read", "#zone_settings:read", "#page_shield:read", "#dns_records:read", "#zone_versioning:read", "#api_gateway:read", "#zaraz:read", "#zone:read", "#web3:read", "#healthchecks:read", "#waitingroom:read", "#access:read", "#ssl:read", "#worker:read", "#logs:read", "#lb:read", "#analytics:read"],
     * "plan": {
     * "id": "94f3b7b768b0458b56d2cac4fe5ec0f9",
     * "name": "Enterprise Website",
     * "price": 0,
     * "currency": "USD",
     * "frequency": "",
     * "is_subscribed": true,
     * "can_subscribe": true,
     * "legacy_id": "enterprise",
     * "legacy_discount": false,
     * "externally_managed": true
     * }
     * }, {
     * "id": "e7ecd6d1ffb63d2dae470342e21c9c7c",
     * "name": "palmpay-inc.com",
     * "status": "active",
     * "paused": false,
     * "type": "partial",
     * "development_mode": 0,
     * "verification_key": "578657688-821532688",
     * "cname_suffix": "cdn.cloudflare.net",
     * "original_name_servers": ["vip3.alidns.com", "vip4.alidns.com"],
     * "original_registrar": "alibaba cloud computing ltd. d (id: 1599)",
     * "original_dnshost": null,
     * "modified_on": "2023-12-06T06:21:51.455654Z",
     * "created_on": "2023-12-06T06:15:12.630995Z",
     * "activated_on": "2023-12-06T06:21:28.472800Z",
     * "meta": {
     * "step": 2,
     * "custom_certificate_quota": 1,
     * "page_rule_quota": 100,
     * "phishing_detected": false,
     * "multiple_railguns_allowed": false
     * },
     * "owner": {
     * "id": "94b62b3fc8103ec370e68661bcf66f41",
     * "type": "organization",
     * "name": "chuanyi——Palmpay Digital Technology"
     * },
     * "account": {
     * "id": "94b62b3fc8103ec370e68661bcf66f41",
     * "name": "chuanyi——Palmpay Digital Technology"
     * },
     * "tenant": {
     * "id": null,
     * "name": null
     * },
     * "tenant_unit": {
     * "id": null
     * },
     * "permissions": ["#waf:read", "#zone_settings:read", "#page_shield:read", "#dns_records:read", "#zone_versioning:read", "#api_gateway:read", "#zaraz:read", "#zone:read", "#web3:read", "#healthchecks:read", "#waitingroom:read", "#access:read", "#ssl:read", "#worker:read", "#logs:read", "#lb:read", "#analytics:read"],
     * "plan": {
     * "id": "94f3b7b768b0458b56d2cac4fe5ec0f9",
     * "name": "Enterprise Website",
     * "price": 0,
     * "currency": "USD",
     * "frequency": "",
     * "is_subscribed": true,
     * "can_subscribe": true,
     * "legacy_id": "enterprise",
     * "legacy_discount": false,
     * "externally_managed": true
     * }
     * }, {
     * "id": "bc23a94e7b81e72ac37ce2b40301b65a",
     * "name": "transspay.net",
     * "status": "active",
     * "paused": false,
     * "type": "partial",
     * "development_mode": 0,
     * "verification_key": "472169294-821296336",
     * "cname_suffix": "cdn.cloudflare.net",
     * "original_name_servers": ["ns-1187.awsdns-20.org", "ns-141.awsdns-17.com", "ns-1582.awsdns-05.co.uk", "ns-809.awsdns-37.net"],
     * "original_registrar": "amazon registrar, inc. (id: 468)",
     * "original_dnshost": null,
     * "modified_on": "2023-12-05T11:04:24.340860Z",
     * "created_on": "2023-12-05T10:50:53.355724Z",
     * "activated_on": "2023-12-05T10:58:09.436745Z",
     * "meta": {
     * "step": 2,
     * "custom_certificate_quota": 1,
     * "page_rule_quota": 100,
     * "phishing_detected": false,
     * "multiple_railguns_allowed": false
     * },
     * "owner": {
     * "id": "94b62b3fc8103ec370e68661bcf66f41",
     * "type": "organization",
     * "name": "chuanyi——Palmpay Digital Technology"
     * },
     * "account": {
     * "id": "94b62b3fc8103ec370e68661bcf66f41",
     * "name": "chuanyi——Palmpay Digital Technology"
     * },
     * "tenant": {
     * "id": null,
     * "name": null
     * },
     * "tenant_unit": {
     * "id": null
     * },
     * "permissions": ["#waf:read", "#zone_settings:read", "#page_shield:read", "#dns_records:read", "#zone_versioning:read", "#api_gateway:read", "#zaraz:read", "#zone:read", "#web3:read", "#healthchecks:read", "#waitingroom:read", "#access:read", "#ssl:read", "#worker:read", "#logs:read", "#lb:read", "#analytics:read"],
     * "plan": {
     * "id": "94f3b7b768b0458b56d2cac4fe5ec0f9",
     * "name": "Enterprise Website",
     * "price": 0,
     * "currency": "USD",
     * "frequency": "",
     * "is_subscribed": true,
     * "can_subscribe": true,
     * "legacy_id": "enterprise",
     * "legacy_discount": false,
     * "externally_managed": true
     * }
     * }],
     * "result_info": {
     * "page": 1,
     * "per_page": 20,
     * "total_pages": 1,
     * "count": 5,
     * "total_count": 5
     * },
     * "success": true,
     * "errors": [],
     * "messages": []
     * }
     */

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
