package com.baiyi.cratos.facade;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.domain.param.http.traffic.TrafficLayerTopologyParam;
import com.baiyi.cratos.domain.view.traffic.TrafficLayerTopologyVO;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

import java.util.Date;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/6/16 16:30
 * &#064;Version 1.0
 */
public class TrafficLayerTopologyFacadeTest extends BaseUnit {

    @Resource
    private TrafficLayerTopologyFacade trafficLayerTopologyFacade;

    @Test
    void test() {
        TrafficLayerTopologyParam.QueryTrafficLayerTopology queryTrafficLayerTopology = TrafficLayerTopologyParam.QueryTrafficLayerTopology.builder()
                .appName("mgw-core")
                .namespace("daily")
                .build();
        Date start = new Date();
        System.out.println(start .getTime());
        TrafficLayerTopologyVO.Topology topology = trafficLayerTopologyFacade.queryTrafficLayerTopology(queryTrafficLayerTopology);
        Date end = new Date();
        System.out.println(end.getTime());
        System.out.println(end.getTime() - start.getTime());
        System.out.println(topology);
    }
}
