package com.baiyi.cratos.eds.dns.impl;

import com.amazonaws.services.route53.model.ResourceRecordSet;
import com.baiyi.cratos.common.enums.TrafficRoutingOptions;
import com.baiyi.cratos.common.exception.TrafficRouteException;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.TrafficRecordTarget;
import com.baiyi.cratos.domain.generator.TrafficRoute;
import com.baiyi.cratos.domain.model.DNS;
import com.baiyi.cratos.domain.param.http.traffic.TrafficRouteParam;
import com.baiyi.cratos.domain.util.dnsgoogle.enums.DnsTypes;
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
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2025/12/11 16:16
 * @Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.AWS)
public class AwsRoute53Resolver extends BaseDNSResolver<EdsConfigs.Aws> {

    public AwsRoute53Resolver(EdsAssetService edsAssetService, TrafficRouteService trafficRouteService,
                              TrafficRecordTargetService trafficRecordTargetService,
                              EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder) {
        super(edsAssetService, trafficRouteService, trafficRecordTargetService, edsInstanceProviderHolderBuilder);
    }

    @Override
    public DNS.ResourceRecordSet getDNSResourceRecordSet(TrafficRoute trafficRoute) {
        // 获取托管区域
        String hostedZoneId = getHostedZoneId(trafficRoute);
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
    public void switchToRoute(TrafficRouteParam.SwitchRecordTarget switchRecordTarget) {
        TrafficRecordTarget trafficRecordTarget = getTrafficRecordTargetById(switchRecordTarget.getRecordTargetId());
        TrafficRoute trafficRoute = getTrafficRouteById(trafficRecordTarget.getTrafficRouteId());
        EdsConfigs.Aws config = getEdsConfig(trafficRoute, EdsAssetTypeEnum.AWS_HOSTED_ZONE);
        String hostedZoneId = getHostedZoneId(trafficRoute);
        if (!StringUtils.hasText(hostedZoneId)) {
            TrafficRouteException.runtime("未找到HostedZoneId");
        }
        List<ResourceRecordSet> resourceRecordSets = AwsRoute53Repo.listResourceRecordSets(config, hostedZoneId);
        DnsTypes dnsType = DnsTypes.valueOf(trafficRoute.getRecordType());
        String domainRecordFqdn = toFQDN(trafficRoute.getDomainRecord());
        Optional<ResourceRecordSet> optionalResourceRecordSet = resourceRecordSets.stream()
                .filter(record -> dnsType.name()
                        .equals(record.getType()) && domainRecordFqdn.equals(record.getName()))
                .findFirst();

        // TODO 
        TrafficRoutingOptions routingOptions = TrafficRoutingOptions.valueOf(switchRecordTarget.getRoutingOptions());
        if (routingOptions.equals(TrafficRoutingOptions.SINGLE_TARGET)) {
            // TODO
        } else {
            TrafficRouteException.runtime("Current operation not implemented");
        }
    }

    private String getHostedZoneId(TrafficRoute trafficRoute) {
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
