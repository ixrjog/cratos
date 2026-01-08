package com.baiyi.cratos.eds.dns.impl;


import com.baiyi.cratos.common.enums.TrafficRoutingOptions;
import com.baiyi.cratos.common.exception.TrafficRouteException;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.TrafficRoute;
import com.baiyi.cratos.domain.model.DNS;
import com.baiyi.cratos.domain.param.http.traffic.TrafficRouteParam;
import com.baiyi.cratos.domain.util.StringFormatter;
import com.baiyi.cratos.eds.cloudflare.model.CloudflareDns;
import com.baiyi.cratos.eds.cloudflare.repo.CloudflareDnsRepo;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.dns.BaseDNSResolver;
import com.baiyi.cratos.eds.dns.SwitchRecordTargetContext;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.TrafficRecordTargetService;
import com.baiyi.cratos.service.TrafficRouteService;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/12/18 14:28
 * &#064;Version 1.0
 */
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.CLOUDFLARE, assetTypeOf = EdsAssetTypeEnum.CLOUDFLARE_ZONE)
public class CloudflareDNSResolver extends BaseDNSResolver<EdsConfigs.Cloudflare, CloudflareDns.DnsRecord> {

    private static final String CONSOLE_URL = "https://dash.cloudflare.com/{}/{}/dns/records";

    public CloudflareDNSResolver(EdsAssetService edsAssetService, TrafficRouteService trafficRouteService,
                                 TrafficRecordTargetService trafficRecordTargetService,
                                 EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder) {
        super(edsAssetService, trafficRouteService, trafficRecordTargetService, edsInstanceProviderHolderBuilder);
    }

    @Override
    public DNS.ResourceRecordSet getDNSResourceRecordSet(TrafficRoute trafficRoute) {
        // 获取阿里云配置
        EdsConfigs.Cloudflare config = getEdsConfig(trafficRoute, getAssetTypeEnum());
        // 查询 DNS 记录
        List<CloudflareDns.DnsRecord> records = CloudflareDnsRepo.listDnsRecords(config, trafficRoute.getZoneId());
        return findMatchedRecord(records, trafficRoute);
    }

    @Override
    protected DNS.ResourceRecordSet findMatchedRecord(List<CloudflareDns.DnsRecord> records,
                                                      TrafficRoute trafficRoute) {
        String domainRecord = trafficRoute.getDomainRecord();
        List<CloudflareDns.DnsRecord> matchedRecords = records.stream()
                .filter(record -> isCnameOrARecord(record.getType()) && domainRecord.equals(record.getName()))
                .toList();
        if (CollectionUtils.isEmpty(matchedRecords)) {
            return DNS.ResourceRecordSet.NO_DATA;
        }
        return DNS.ResourceRecordSet.builder()
                //.type(dnsRRType.name())
                .name(domainRecord)
                .resourceRecords(toResourceRecords(matchedRecords))
                .build();
    }

    private List<DNS.ResourceRecord> toResourceRecords(List<CloudflareDns.DnsRecord> records) {
        return records.stream()
                .map(record -> DNS.ResourceRecord.builder()
                        .value(record.getContent())
                        .proxied(record.getProxied())
                        .build())
                .toList();
    }

    @Override
    public void switchToRoute(TrafficRouteParam.SwitchRecordTarget switchRecordTarget) {
        TrafficRoutingOptions routingOptions = TrafficRoutingOptions.valueOf(switchRecordTarget.getRoutingOptions());
        SwitchRecordTargetContext<EdsConfigs.Cloudflare, CloudflareDns.DnsRecord> context = buildSwitchContext(
                switchRecordTarget);
        if (routingOptions.equals(TrafficRoutingOptions.SINGLE_TARGET)) {
            handleSingleTargetRouting(context);
        } else {
            TrafficRouteException.runtime("Current operation not implemented.");
        }
    }

    @Override
    protected Map<String, List<CloudflareDns.DnsRecord>> toMatchedRecordMap(List<CloudflareDns.DnsRecord> records) {
        return records.stream()
                .collect(Collectors.groupingBy(CloudflareDns.DnsRecord::getType));
    }

    @Override
    protected void handleSingleTargetRouting(
            SwitchRecordTargetContext<EdsConfigs.Cloudflare, CloudflareDns.DnsRecord> context) {
        final String zoneId = getZoneId(context.getTrafficRoute());
        if (context.isMatchedRecordsEmpty()) {
            CloudflareDnsRepo.createDnsRecord(
                    context.getConfig(), zoneId, context.getResourceRecord(), context.getTTL(), context.getDnsRRType()
                            .name(), context.getRecordValue(), context.getProxied(), "Created by Cratos"
            );
            return;
        }
        if (context.getMatchedRecordsSize() == 1) {
            CloudflareDnsRepo.updateDnsRecord(
                    context.getConfig(), zoneId, context.getMatchedRecords()
                            .getFirst()
                            .getId(), context.getResourceRecord(), context.getTTL(), context.getDnsRRType()
                            .name(), context.getRecordValue(), context.getProxied(), "Updated by Cratos"
            );
        } else {
            handleMultipleRecords(zoneId, context);
        }
    }

    private void handleMultipleRecords(String zoneId,
                                       SwitchRecordTargetContext<EdsConfigs.Cloudflare, CloudflareDns.DnsRecord> context) {
        Optional<CloudflareDns.DnsRecord> optionalRecord = context.getMatchedRecords()
                .stream()
                .filter(e -> e.getContent()
                        .equals(context.getRecordValue()))
                .findFirst();
        // 找到当前解析
        if (optionalRecord.isPresent()) {
            updateAndDeleteOthers(zoneId, context, optionalRecord.get());
        } else {
            addAndDeleteAll(zoneId, context);
        }
    }

    private void updateAndDeleteOthers(String zoneId,
                                       SwitchRecordTargetContext<EdsConfigs.Cloudflare, CloudflareDns.DnsRecord> context,
                                       CloudflareDns.DnsRecord targetRecord) {
        final String recordId = targetRecord.getId();
        CloudflareDnsRepo.updateDnsRecord(
                context.getConfig(), zoneId, recordId, context.getResourceRecord(), context.getTTL(),
                context.getDnsRRType()
                        .name(), context.getRecordValue(), context.getProxied(), "Updated by Cratos"
        );
        context.getMatchedRecords()
                .stream()
                .map(CloudflareDns.DnsRecord::getId)
                .filter(id -> !id.equals(recordId))
                .forEach(id -> CloudflareDnsRepo.deleteDnsRecord(context.getConfig(), zoneId, id));
    }

    private void addAndDeleteAll(String zoneId,
                                 SwitchRecordTargetContext<EdsConfigs.Cloudflare, CloudflareDns.DnsRecord> context) {
        CloudflareDnsRepo.createDnsRecord(
                context.getConfig(), zoneId, context.getResourceRecord(), context.getTTL(), context.getDnsRRType()
                        .name(), context.getRecordValue(), context.getProxied(), "Created by Cratos"
        );
        context.getMatchedRecords()
                .forEach(record -> CloudflareDnsRepo.deleteDnsRecord(context.getConfig(), zoneId, record.getId()));
    }

    @Override
    protected List<CloudflareDns.DnsRecord> getTrafficRouteRecords(EdsConfigs.Cloudflare config,
                                                                   TrafficRoute trafficRoute) {
        List<CloudflareDns.DnsRecord> records = CloudflareDnsRepo.listDnsRecords(config, trafficRoute.getZoneId());
        if (CollectionUtils.isEmpty(records)) {
            return List.of();
        }
        String domainRecord = trafficRoute.getDomainRecord();
        return records.stream()
                .filter(record -> isCnameOrARecord(record.getType()) && domainRecord.equals(record.getName()))
                .toList();
    }

    @Override
    public String getZoneId(TrafficRoute trafficRoute) {
        if (StringUtils.hasText(trafficRoute.getZoneId())) {
            return trafficRoute.getZoneId();
        }
        List<EdsAsset> hostedZoneAssets = edsAssetService.queryInstanceAssetByTypeAndName(
                trafficRoute.getDnsResolverInstanceId(), EdsAssetTypeEnum.CLOUDFLARE_ZONE.name(),
                trafficRoute.getDomain(), false
        );
        return CollectionUtils.isEmpty(hostedZoneAssets) ? null : hostedZoneAssets.getFirst()
                .getAssetId();
    }

    @Override
    public String generateConsoleURL(TrafficRoute trafficRoute) {
        EdsConfigs.Cloudflare config = getEdsConfig(trafficRoute, EdsAssetTypeEnum.CLOUDFLARE_ZONE);
        return StringFormatter.arrayFormat(
                CONSOLE_URL, config.getCred()
                        .getAccountId(), trafficRoute.getDomain()
        );
    }

}
