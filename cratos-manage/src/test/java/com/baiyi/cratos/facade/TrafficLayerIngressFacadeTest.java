package com.baiyi.cratos.facade;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.domain.param.traffic.TrafficLayerIngressParam;
import com.baiyi.cratos.domain.view.traffic.TrafficLayerIngressVO;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/8/14 下午6:30
 * &#064;Version 1.0
 */
public class TrafficLayerIngressFacadeTest extends BaseUnit {

    @Resource
    private TrafficLayerIngressFacade trafficLayerIngressFacade;


    @Test
    void test() {
        TrafficLayerIngressParam.QueryIngressHostDetails queryIngressHostDetails = TrafficLayerIngressParam.QueryIngressHostDetails.builder()
                // tz-pos-dev.palmpay-inc.com
                .queryHost("tz-pos-dev.palmpay-inc.com")
                .build();


        TrafficLayerIngressVO.IngressDetails details = trafficLayerIngressFacade.queryIngressHostDetails(
                queryIngressHostDetails);

        System.out.println(details);

    }
}
