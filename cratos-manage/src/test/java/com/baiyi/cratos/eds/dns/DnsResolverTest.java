package com.baiyi.cratos.eds.dns;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.common.enums.TrafficRoutingOptions;
import com.baiyi.cratos.domain.param.http.traffic.TrafficRouteParam;
import com.baiyi.cratos.eds.dns.impl.AliyunDnsResolver;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/12/16 11:32
 * &#064;Version 1.0
 */
public class AliyunDnsResolverTest extends BaseUnit {

    @Resource
    private AliyunDnsResolver aliyunDnsResolver;

    @Test
    void test1() {
        TrafficRouteParam.SwitchRecordTarget switchRecordTarget = TrafficRouteParam.SwitchRecordTarget.builder()
                .recordTargetId(5)
                .routingOptions(TrafficRoutingOptions.SINGLE_TARGET.name())
                .build();
        aliyunDnsResolver.switchToRoute(switchRecordTarget);
    }

    @Test
    void test2() {
        TrafficRouteParam.SwitchRecordTarget switchRecordTarget = TrafficRouteParam.SwitchRecordTarget.builder()
                .recordTargetId(6)
                .routingOptions(TrafficRoutingOptions.SINGLE_TARGET.name())
                .build();
        aliyunDnsResolver.switchToRoute(switchRecordTarget);
    }

}
