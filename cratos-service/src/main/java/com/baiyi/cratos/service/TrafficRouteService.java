package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.TrafficRoute;
import com.baiyi.cratos.domain.param.http.traffic.TrafficRouteParam;
import com.baiyi.cratos.mapper.TrafficRouteMapper;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;
import com.baiyi.cratos.service.base.BaseValidService;
import com.baiyi.cratos.service.base.SupportBusinessService;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/12/11 09:50
 * &#064;Version 1.0
 */
public interface TrafficRouteService extends BaseUniqueKeyService<TrafficRoute, TrafficRouteMapper>, BaseValidService<TrafficRoute, TrafficRouteMapper>, SupportBusinessService {

    DataTable<TrafficRoute> queryPageByParam(TrafficRouteParam.RoutePageQueryParam pageQuery);

}
