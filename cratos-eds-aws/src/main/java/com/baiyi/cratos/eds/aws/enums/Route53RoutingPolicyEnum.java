package com.baiyi.cratos.eds.aws.enums;

import com.amazonaws.services.route53.model.ResourceRecordSet;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/12/16 15:11
 * &#064;Version 1.0
 */
public enum Route53RoutingPolicyEnum {

    SIMPLE,
    ALIAS,
    MULTIVALUE_ANSWER,
    FAILOVER,
    GEOPROXIMITY,
    GEOLOCATION,
    LATENCY,
    WEIGHTED;

    public static Route53RoutingPolicyEnum getRoutingPolicy(ResourceRecordSet recordSet) {
        // 1. 加权路由策略
        if (recordSet.getWeight() != null) {
            return WEIGHTED;
        }

        // 2. 延迟路由策略
        if (recordSet.getRegion() != null) {
            return LATENCY;
        }

        // 3. 地理位置路由策略
        if (recordSet.getGeoLocation() != null) {
            return GEOLOCATION;
        }

        // 4. 地理邻近路由策略
        if (recordSet.getGeoProximityLocation() != null) {
            return GEOPROXIMITY;
        }

        // 5. 故障转移路由策略
        if (recordSet.getFailover() != null) {
            return FAILOVER;
        }

        // 6. 多值应答路由策略
        if (recordSet.getMultiValueAnswer() != null && recordSet.getMultiValueAnswer()) {
            return MULTIVALUE_ANSWER;
        }

        // 7. ALIAS 记录
        if (recordSet.getAliasTarget() != null) {
            return ALIAS;
        }

        // 8. 简单路由策略（默认）
        return SIMPLE;
    }

}
