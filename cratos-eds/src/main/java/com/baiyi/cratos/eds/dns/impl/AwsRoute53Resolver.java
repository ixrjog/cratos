package com.baiyi.cratos.eds.dns.impl;

import com.amazonaws.services.route53.model.*;
import com.baiyi.cratos.common.enums.TrafficRoutingOptions;
import com.baiyi.cratos.common.exception.TrafficRouteException;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.TrafficRecordTarget;
import com.baiyi.cratos.domain.generator.TrafficRoute;
import com.baiyi.cratos.domain.model.DNS;
import com.baiyi.cratos.domain.param.http.traffic.TrafficRouteParam;
import com.baiyi.cratos.domain.util.dnsgoogle.enums.DnsTypes;
import com.baiyi.cratos.eds.aws.enums.Route53RoutingPolicyEnum;
import com.baiyi.cratos.eds.aws.repo.AwsRoute53Repo;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.dns.BaseDNSResolver;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.TrafficRecordTargetService;
import com.baiyi.cratos.service.TrafficRouteService;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2025/12/11 16:16
 * @Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.AWS)
public class AwsRoute53Resolver extends BaseDNSResolver<EdsConfigs.Aws, ResourceRecordSet> {

    public AwsRoute53Resolver(EdsAssetService edsAssetService, TrafficRouteService trafficRouteService,
                              TrafficRecordTargetService trafficRecordTargetService,
                              EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder) {
        super(edsAssetService, trafficRouteService, trafficRecordTargetService, edsInstanceProviderHolderBuilder);
    }

    @Override
    public DNS.ResourceRecordSet getDNSResourceRecordSet(TrafficRoute trafficRoute) {
        // 获取托管区域
        String hostedZoneId = getZoneId(trafficRoute);
        if (!StringUtils.hasText(hostedZoneId)) {
            return DNS.ResourceRecordSet.NO_DATA;
        }
        // 获取 AWS 配置
        EdsConfigs.Aws config = getEdsConfig(trafficRoute, EdsAssetTypeEnum.AWS_HOSTED_ZONE);
        // 查询 DNS 记录
        List<ResourceRecordSet> resourceRecordSets = AwsRoute53Repo.listResourceRecordSets(config, hostedZoneId);
        // 查找匹配的记录
        return findMatchingRecord(resourceRecordSets, trafficRoute);
    }

    @Override
    protected List<ResourceRecordSet> getTrafficRouteRecords(EdsConfigs.Aws config, TrafficRoute trafficRoute) {
        String hostedZoneId = getZoneId(trafficRoute);
        String domainRecordFqdn = toFQDN(trafficRoute.getDomainRecord());
        DnsTypes dnsType = DnsTypes.valueOf(trafficRoute.getRecordType());
        List<ResourceRecordSet> resourceRecordSets = AwsRoute53Repo.listResourceRecordSets(config, hostedZoneId);
        return resourceRecordSets.stream()
                .filter(record -> dnsType.name()
                        .equals(record.getType()) && domainRecordFqdn.equals(record.getName()))
                .toList();
    }

    @Override
    public void switchToRoute(TrafficRouteParam.SwitchRecordTarget switchRecordTarget) {
        TrafficRecordTarget trafficRecordTarget = getTrafficRecordTargetById(switchRecordTarget.getRecordTargetId());
        TrafficRoute trafficRoute = getTrafficRouteById(trafficRecordTarget.getTrafficRouteId());
        EdsConfigs.Aws config = getEdsConfig(trafficRoute, EdsAssetTypeEnum.AWS_HOSTED_ZONE);
        String hostedZoneId = getZoneId(trafficRoute);
        if (!StringUtils.hasText(hostedZoneId)) {
            TrafficRouteException.runtime("未找到HostedZoneId");
        }
        DnsTypes dnsType = DnsTypes.valueOf(trafficRoute.getRecordType());
        String domainRecordFqdn = toFQDN(trafficRoute.getDomainRecord());
        List<ResourceRecordSet> matchedRecords = getTrafficRouteRecords(config, trafficRoute);
        validateRecordCount(matchedRecords);
        TrafficRoutingOptions routingOptions = TrafficRoutingOptions.valueOf(switchRecordTarget.getRoutingOptions());
        if (routingOptions.equals(TrafficRoutingOptions.SINGLE_TARGET)) {
            // 删除老记录
            deleteRecords(config, hostedZoneId, trafficRecordTarget, dnsType, matchedRecords);
            // 新增简单路由记录
            addSimpleRecord(config, hostedZoneId, trafficRecordTarget, dnsType);
        } else {
            TrafficRouteException.runtime("Current operation not implemented");
        }
    }

    private void addSimpleRecord(EdsConfigs.Aws config, String hostedZoneId, TrafficRecordTarget trafficRecordTarget,
                                 DnsTypes dnsType) {
        ResourceRecordSet rrs = new ResourceRecordSet(
                toFQDN(trafficRecordTarget.getResourceRecord()), RRType.valueOf(dnsType.name()));
        long ttl = trafficRecordTarget.getTtl() != null ? trafficRecordTarget.getTtl() : 300L;
        rrs.setTTL(ttl);
        rrs.setResourceRecords(List.of(new ResourceRecord(trafficRecordTarget.getRecordValue())));
        Change upsertChange = new Change(ChangeAction.UPSERT, rrs);
        AwsRoute53Repo.changeResourceRecordSets(config, hostedZoneId, List.of(upsertChange));
    }

    // 前置删除解析
    private void deleteRecords(EdsConfigs.Aws config, String hostedZoneId, TrafficRecordTarget trafficRecordTarget,
                               DnsTypes dnsType, List<ResourceRecordSet> matchedRecords) {
        // 没有记录
        if (CollectionUtils.isEmpty(matchedRecords)) {
            return;
        }
        ResourceRecordSet rrs = matchedRecords.getFirst();
        Route53RoutingPolicyEnum routingPolicy = Route53RoutingPolicyEnum.getRoutingPolicy(rrs);
        // 单条记录
        if (matchedRecords.size() == 1) {
            // 简单路由 不需要操作
            if (Route53RoutingPolicyEnum.SIMPLE == routingPolicy) {
                return;
            }
        }
        // 删除加权路由
        if (Route53RoutingPolicyEnum.WEIGHTED == routingPolicy) {
            List<Change> changes = matchedRecords.stream()
                    .map(e -> new Change(ChangeAction.DELETE, e))
                    .toList();
            AwsRoute53Repo.changeResourceRecordSets(config, hostedZoneId, changes);
            return;
        }
        // 其它复杂路由
        TrafficRouteException.runtime(
                "Current routing mode {} is not supported for changes, only simple routing mode or weighted routing is supported.",
                routingPolicy.name()
        );
    }

    @Override
    public String getZoneId(TrafficRoute trafficRoute) {
        if (StringUtils.hasText(trafficRoute.getZoneId())) {
            return trafficRoute.getZoneId();
        }
        String domainFqdn = toFQDN(trafficRoute.getDomain());
        List<EdsAsset> hostedZoneAssets = edsAssetService.queryInstanceAssetByTypeAndName(
                trafficRoute.getDnsResolverInstanceId(), EdsAssetTypeEnum.AWS_HOSTED_ZONE.name(), domainFqdn, false);
        return CollectionUtils.isEmpty(hostedZoneAssets) ? null : hostedZoneAssets.getFirst()
                .getAssetId();
    }

    private DNS.ResourceRecordSet findMatchingRecord(List<ResourceRecordSet> resourceRecordSets,
                                                     TrafficRoute trafficRoute) {
        String recordType = trafficRoute.getRecordType();
        String domainRecordFqdn = toFQDN(trafficRoute.getDomainRecord());
        return resourceRecordSets.stream()
                .filter(record -> recordType.equals(record.getType()) && domainRecordFqdn.equals(record.getName()))
                .map(this::convertToResourceRecordSet)
                .findFirst()
                .orElse(DNS.ResourceRecordSet.NO_DATA);
    }

    private DNS.ResourceRecordSet convertToResourceRecordSet(ResourceRecordSet record) {
        return DNS.ResourceRecordSet.builder()
                .type(record.getType())
                .name(record.getName()
                              .replaceAll("\\.$", "")) // 移除末尾的点
                .resourceRecords(record.getResourceRecords()
                                         .stream()
                                         .map(r -> DNS.ResourceRecord.builder()
                                                 .value(r.getValue())
                                                 .tTL(record.getTTL())
                                                 .weight(record.getWeight())
                                                 .build())
                                         .toList())
                .build();
    }

}
