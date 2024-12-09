package com.baiyi.cratos.facade;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.http.traffic.TrafficIngressTrafficLimitParam;
import com.baiyi.cratos.domain.view.traffic.TrafficLayerIngressVO;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/9 14:48
 * &#064;Version 1.0
 */
public class TrafficLayerIngressTrafficLimitFacadeTest extends BaseUnit {

    @Resource
    private TrafficLayerIngressTrafficLimitFacade trafficLayerIngressTrafficLimitFacade;

    @Test
    void test() {
        TrafficIngressTrafficLimitParam.IngressTrafficLimitPageQuery pageQuery = TrafficIngressTrafficLimitParam.IngressTrafficLimitPageQuery.builder()
                .page(1)
                .length(10)
                .build();
        DataTable<TrafficLayerIngressVO.IngressTrafficLimit> table = trafficLayerIngressTrafficLimitFacade.queryIngressTrafficLimitPage(
                pageQuery);
        System.out.println(table);
    }

}
