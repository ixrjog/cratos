package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.TrafficRoute;
import com.baiyi.cratos.domain.param.http.traffic.TrafficRouteParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TrafficRouteMapper extends Mapper<TrafficRoute> {

    List<TrafficRoute> queryPageByParam(TrafficRouteParam.RoutePageQueryParam pageQuery);

}