package com.baiyi.cratos.common.enums;

import lombok.Getter;

/**
 * @Author baiyi
 * @Date 2024/3/28 11:12
 * @Version 1.0
 */
@Getter
public enum TrafficLayerResourceTypeEnum {

    CDN("CDN"),
    DDOS("DDoS"),
    WAF("WAF"),
    CLOUDFLARE("CloudFlare"),
    DOMAIN("Domain");

    private final String displayName;

    TrafficLayerResourceTypeEnum(String displayName) {
        this.displayName = displayName;
    }

}
