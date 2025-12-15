package com.baiyi.cratos.facade.traffic;

import com.baiyi.cratos.annotation.PageQueryByTag;
import com.baiyi.cratos.common.enums.TrafficRecordTargetTypes;
import com.baiyi.cratos.common.exception.TrafficRouteException;
import com.baiyi.cratos.common.util.IpUtils;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.TrafficRecordTarget;
import com.baiyi.cratos.domain.generator.TrafficRoute;
import com.baiyi.cratos.domain.param.http.traffic.TrafficRouteParam;
import com.baiyi.cratos.domain.util.dnsgoogle.enums.DnsTypes;
import com.baiyi.cratos.domain.view.traffic.TrafficRouteVO;
import com.baiyi.cratos.facade.TrafficRouteFacade;
import com.baiyi.cratos.service.TrafficRecordTargetService;
import com.baiyi.cratos.service.TrafficRouteService;
import com.baiyi.cratos.wrapper.traffic.TrafficRouteWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;

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
    private final TrafficRecordTargetService trafficRecordTargetService;
    private final TrafficRouteWrapper routeWrapper;

    @Override
    @PageQueryByTag(typeOf = BusinessTypeEnum.TRAFFIC_ROUTE)
    public DataTable<TrafficRouteVO.Route> queryRoutePage(TrafficRouteParam.RoutePageQuery pageQuery) {
        DataTable<TrafficRoute> table = trafficRouteService.queryPageByParam(pageQuery.toParam());
        return routeWrapper.wrapToTarget(table);
    }

    @Override
    public void addTrafficRoute(TrafficRouteParam.AddRoute addRoute) {
        validateRecordType(addRoute.getRecordType());
        TrafficRoute trafficRoute = addRoute.toTarget();
        trafficRouteService.add(trafficRoute);
    }

    private void validateRecordType(String recordType) {
        try {
            DnsTypes dnsType = DnsTypes.valueOf(recordType);
            if (!Set.of(DnsTypes.CNAME, DnsTypes.A)
                    .contains(dnsType)) {
                throw new TrafficRouteException("Invalid DNS record type");
            }
        } catch (IllegalArgumentException e) {
            throw new TrafficRouteException("Invalid DNS record type");
        }
    }

    @Override
    public void updateTrafficRoute(TrafficRouteParam.UpdateRoute updateRoute) {
        TrafficRoute trafficRoute = updateRoute.toTarget();
        trafficRouteService.updateByPrimaryKey(trafficRoute);
    }

    @Override
    public void addTrafficRecordTarget(TrafficRouteParam.AddRecordTarget addRouteTarget) {
        TrafficRecordTarget trafficRecordTarget = addRouteTarget.toTarget();
        TrafficRoute trafficRoute = trafficRouteService.getById(trafficRecordTarget.getTrafficRouteId());
        if (trafficRoute == null) {
            TrafficRouteException.runtime(
                    "TrafficRoute 配置不存在: trafficRouteId = {}", trafficRecordTarget.getTrafficRouteId());
        }
        trafficRecordTarget.setResourceRecord(trafficRoute.getDomainRecord());
        validateRecordType(trafficRecordTarget.getRecordType());
        if (DnsTypes.CNAME.name()
                .equals(trafficRecordTarget.getRecordType())) {
            // 校验 recordValue 是否是域名
        }
        if (DnsTypes.A.name()
                .equals(trafficRecordTarget.getRecordType())) {
            if (!IpUtils.isIP(trafficRecordTarget.getRecordValue())) {
                TrafficRouteException.runtime("Invalid IP address.");
            }
        }
        try {
            TrafficRecordTargetTypes.valueOf(trafficRecordTarget.getTargetType());
        } catch (IllegalArgumentException e) {
            throw new TrafficRouteException("Invalid target type.");
        }
        trafficRecordTargetService.add(trafficRecordTarget);
    }

}
