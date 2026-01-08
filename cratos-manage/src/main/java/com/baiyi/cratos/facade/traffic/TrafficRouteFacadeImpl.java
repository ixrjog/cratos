package com.baiyi.cratos.facade.traffic;

import com.baiyi.cratos.annotation.PageQueryByTag;
import com.baiyi.cratos.common.enums.SysTagKeys;
import com.baiyi.cratos.common.enums.TrafficRecordTargetTypes;
import com.baiyi.cratos.common.exception.TrafficRouteException;
import com.baiyi.cratos.common.util.IpUtils;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.generator.Tag;
import com.baiyi.cratos.domain.generator.TrafficRecordTarget;
import com.baiyi.cratos.domain.generator.TrafficRoute;
import com.baiyi.cratos.domain.param.http.eds.EdsInstanceParam;
import com.baiyi.cratos.domain.param.http.tag.BusinessTagParam;
import com.baiyi.cratos.domain.param.http.traffic.TrafficRouteParam;
import com.baiyi.cratos.domain.view.eds.EdsInstanceVO;
import com.baiyi.cratos.domain.view.traffic.TrafficRouteVO;
import com.baiyi.cratos.eds.dns.DNSResolver;
import com.baiyi.cratos.eds.dns.DNSResolverFactory;
import com.baiyi.cratos.eds.dnsgoogle.enums.DnsRRType;
import com.baiyi.cratos.facade.EdsFacade;
import com.baiyi.cratos.facade.TrafficRouteFacade;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.service.TagService;
import com.baiyi.cratos.service.TrafficRecordTargetService;
import com.baiyi.cratos.service.TrafficRouteService;
import com.baiyi.cratos.wrapper.traffic.TrafficRouteWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
    private final EdsInstanceService edsInstanceService;
    private final EdsFacade edsFacade;
    private final TagService tagService;

    @Override
    @PageQueryByTag(typeOf = BusinessTypeEnum.TRAFFIC_ROUTE)
    public DataTable<TrafficRouteVO.Route> queryRoutePage(TrafficRouteParam.RoutePageQuery pageQuery) {
        DataTable<TrafficRoute> table = trafficRouteService.queryPageByParam(pageQuery.toParam());
        return routeWrapper.wrapToTarget(table);
    }

    @Override
    public void switchToTarget(TrafficRouteParam.SwitchRecordTarget switchRecordTarget) {
        TrafficRecordTarget trafficRecordTarget = Optional.ofNullable(
                        trafficRecordTargetService.getById(switchRecordTarget.getRecordTargetId()))
                .orElseThrow(() -> new TrafficRouteException("Traffic record target id not found"));
        TrafficRoute trafficRoute = Optional.ofNullable(
                        trafficRouteService.getById(trafficRecordTarget.getTrafficRouteId()))
                .orElseThrow(() -> new TrafficRouteException("Traffic route id not found"));
        EdsInstance edsInstance = Optional.ofNullable(
                        edsInstanceService.getById(trafficRoute.getDnsResolverInstanceId()))
                .orElseThrow(() -> new TrafficRouteException("Eds instance id not found"));
        DNSResolver dnsResolver = Optional.ofNullable(DNSResolverFactory.getDNSResolver(edsInstance.getEdsType()))
                .orElseThrow(() -> new TrafficRouteException("DNS resolver type not found"));
        dnsResolver.switchToRoute(switchRecordTarget);
    }

    @Override
    public TrafficRouteVO.Route getTrafficRouteById(int id) {
        TrafficRoute trafficRoute = Optional.ofNullable(trafficRouteService.getById(id))
                .orElseThrow(() -> new TrafficRouteException("Traffic route id not found"));
        return routeWrapper.wrapToTarget(trafficRoute);
    }

    @Override
    public List<EdsInstanceVO.EdsInstance> queryDnsResolverInstances() {
        Tag tag = tagService.getByTagKey(SysTagKeys.DNS_RESOLVER);
        if (Objects.isNull(tag)) {
            return List.of();
        }
        BusinessTagParam.QueryByTag queryByTag = BusinessTagParam.QueryByTag.builder()
                .tagId(tag.getId())
                .businessType(BusinessTypeEnum.EDS_INSTANCE.name())
                .build();
        EdsInstanceParam.InstancePageQuery pageQuery = EdsInstanceParam.InstancePageQuery.builder()
                .page(1)
                .length(16)
                .queryByTag(queryByTag)
                .build();
        DataTable<EdsInstanceVO.EdsInstance> table = edsFacade.queryEdsInstancePage(pageQuery);
        return table.getData();
    }

    @Override
    public void addTrafficRoute(TrafficRouteParam.AddRoute addRoute) {
        validateRecordType(addRoute.getRecordType());
        TrafficRoute trafficRoute = addRoute.toTarget();
        EdsInstance edsInstance = Optional.ofNullable(
                        edsInstanceService.getById(trafficRoute.getDnsResolverInstanceId()))
                .orElseThrow(() -> new TrafficRouteException("Eds instance id not found"));
        DNSResolver dnsResolver = DNSResolverFactory.getDNSResolver(edsInstance.getEdsType());
        String zoneId = dnsResolver.getZoneId(trafficRoute);
        trafficRoute.setZoneId(zoneId);
        trafficRouteService.add(trafficRoute);
    }

    private void validateRecordType(String recordType) {
        try {
            DnsRRType dnsType = DnsRRType.valueOf(recordType);
            if (!Set.of(DnsRRType.CNAME, DnsRRType.A)
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
        TrafficRoute trafficRoute = Optional.ofNullable(
                        trafficRouteService.getById(trafficRecordTarget.getTrafficRouteId()))
                .orElseThrow(() -> new TrafficRouteException(
                        "TrafficRoute 配置不存在: trafficRouteId = {}",
                        trafficRecordTarget.getTrafficRouteId()
                ));
        trafficRecordTarget.setResourceRecord(trafficRoute.getDomainRecord());
        validateRecordType(trafficRecordTarget.getRecordType());
        if (DnsRRType.CNAME.name()
                .equals(trafficRecordTarget.getRecordType())) {
            // 校验 recordValue 是否是域名
        }
        if (DnsRRType.A.name()
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

    @Override
    public void updateTrafficRecordTarget(TrafficRouteParam.UpdateRecordTarget updateRecordTarget) {
        TrafficRecordTarget trafficRecordTarget = Optional.ofNullable(
                        trafficRecordTargetService.getById(updateRecordTarget.getId()))
                .orElseThrow(() -> new TrafficRouteException(
                        "TrafficRecordTarget  配置不存在: id = {}",
                        updateRecordTarget.getId()
                ));
        trafficRecordTarget.setRecordValue(updateRecordTarget.getRecordValue());
        trafficRecordTarget.setTtl(updateRecordTarget.getTtl());
        trafficRecordTarget.setRecordType(updateRecordTarget.getRecordType());
        trafficRecordTarget.setWeight(updateRecordTarget.getWeight());
        trafficRecordTargetService.updateByPrimaryKey(trafficRecordTarget);
    }

    @Override
    public void deleteTrafficRouteById(int id) {
        if (!CollectionUtils.isEmpty(trafficRecordTargetService.queryByTrafficRouteId(id))) {
            throw new TrafficRouteException("Please delete TrafficRecordTarget before executing route deletion.");
        }
        trafficRouteService.deleteById(id);
    }

    @Override
    public void deleteTrafficRecordTargetById(int id) {
        trafficRecordTargetService.deleteById(id);
    }

}
