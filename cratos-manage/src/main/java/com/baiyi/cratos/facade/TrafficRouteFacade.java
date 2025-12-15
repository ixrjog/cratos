package com.baiyi.cratos.facade;


import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.http.traffic.TrafficRouteParam;
import com.baiyi.cratos.domain.view.traffic.TrafficRouteVO;


/**
 * &#064;Author  baiyi
 * &#064;Date  2025/12/11 09:47
 * &#064;Version 1.0
 */
public interface TrafficRouteFacade {

    DataTable<TrafficRouteVO.Route> queryRoutePage(TrafficRouteParam.RoutePageQuery pageQuery);

    void addTrafficRoute(TrafficRouteParam.AddRoute addRoute);

    void updateTrafficRoute(TrafficRouteParam.UpdateRoute updateRoute);

    void addTrafficRecordTarget(TrafficRouteParam.AddRecordTarget addRouteTarget);

}
