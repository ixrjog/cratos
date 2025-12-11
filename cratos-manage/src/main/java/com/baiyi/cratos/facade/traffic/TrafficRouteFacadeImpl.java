package com.baiyi.cratos.facade.traffic;

import com.baiyi.cratos.annotation.PageQueryByTag;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.TrafficRoute;
import com.baiyi.cratos.domain.param.http.traffic.TrafficRouteParam;
import com.baiyi.cratos.domain.view.traffic.TrafficRouteVO;
import com.baiyi.cratos.facade.TrafficRouteFacade;
import com.baiyi.cratos.service.TrafficRouteService;
import com.baiyi.cratos.wrapper.traffic.TrafficRouteWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/12/11 09:48
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TrafficRouteFacadeImpl implements TrafficRouteFacade {

    private final TrafficRouteService trafficRouteService;
    private final TrafficRouteWrapper routeWrapper;

    @PageQueryByTag(typeOf = BusinessTypeEnum.TRAFFIC_ROUTE)
    public DataTable<TrafficRouteVO.Route> queryRoutePage(TrafficRouteParam.RoutePageQuery pageQuery) {
        DataTable<TrafficRoute> table = trafficRouteService.queryPageByParam(pageQuery.toParam());
        return routeWrapper.wrapToTarget(table);
    }

}
