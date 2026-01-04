package com.baiyi.cratos.facade;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.http.traffic.TrafficRouteParam;
import com.baiyi.cratos.domain.view.traffic.TrafficRouteVO;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/12/12 14:35
 * &#064;Version 1.0
 */
public class TrafficRouteFacadeTest extends BaseUnit {

    @Resource
    private TrafficRouteFacade trafficRouteFacade;

    @Test
    void test1() {
        TrafficRouteParam.RoutePageQuery pageQuery = TrafficRouteParam.RoutePageQuery.builder()
                .page(1)
                .length(1)
                .build();
        DataTable<TrafficRouteVO.Route> dataTable = trafficRouteFacade.queryRoutePage(pageQuery);
        System.out.println(dataTable.getData());
    }


    @Test
    void test2() {
        TrafficRouteVO.Route route = trafficRouteFacade.getTrafficRouteById(10);
        System.out.println(route);
    }

}
