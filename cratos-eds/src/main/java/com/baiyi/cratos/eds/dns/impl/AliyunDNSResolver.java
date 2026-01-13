package com.baiyi.cratos.eds.dns.impl;

import com.aliyun.sdk.service.alidns20150109.models.DescribeDomainRecordsResponseBody;
import com.baiyi.cratos.common.enums.TrafficRoutingOptions;
import com.baiyi.cratos.common.exception.TrafficRouteException;
import com.baiyi.cratos.domain.generator.TrafficRoute;
import com.baiyi.cratos.domain.model.DNS;
import com.baiyi.cratos.domain.param.http.traffic.TrafficRouteParam;
import com.baiyi.cratos.domain.util.StringFormatter;
import com.baiyi.cratos.eds.aliyun.repo.AliyunDnsRepo;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.dns.BaseDNSResolver;
import com.baiyi.cratos.eds.dns.SwitchRecordTargetContext;
import com.baiyi.cratos.eds.dnsgoogle.enums.DnsRRType;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.TrafficRecordTargetService;
import com.baiyi.cratos.service.TrafficRouteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2025/12/12 10:19
 * @Version 1.0
 */
@Slf4j
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.ALIYUN, assetTypeOf = EdsAssetTypeEnum.ALIYUN_DOMAIN)
public class AliyunDNSResolver extends BaseDNSResolver<EdsConfigs.Aliyun, DescribeDomainRecordsResponseBody.Record> {

    private static final String CONSOLE_URL = "https://dnsnext.console.aliyun.com/authoritative/domains/{}?RRKeyWord={}";

    public AliyunDNSResolver(EdsAssetService edsAssetService, TrafficRouteService trafficRouteService,
                             TrafficRecordTargetService trafficRecordTargetService,
                             EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder) {
        super(edsAssetService, trafficRouteService, trafficRecordTargetService, edsInstanceProviderHolderBuilder);
    }

    @Override
    public DNS.ResourceRecordSet getDNSResourceRecordSet(TrafficRoute trafficRoute) {
        // 获取阿里云配置
        EdsConfigs.Aliyun config = getEdsConfig(trafficRoute, getAssetTypeEnum());
        // 查询 DNS 记录
        List<DescribeDomainRecordsResponseBody.Record> records = AliyunDnsRepo.describeDomainRecords(
                config, trafficRoute.getDomain());
        // 查找匹配的记录
        return findMatchedRecord(records, trafficRoute);
    }

    @Override
    protected Map<String, List<DescribeDomainRecordsResponseBody.Record>> toMatchedRecordMap(
            List<DescribeDomainRecordsResponseBody.Record> records) {
        return records.stream()
                .collect(Collectors.groupingBy(DescribeDomainRecordsResponseBody.Record::getType));
    }

    @Override
    public void switchToRoute(TrafficRouteParam.SwitchRecordTarget switchRecordTarget) {
        TrafficRoutingOptions routingOptions = TrafficRoutingOptions.valueOf(switchRecordTarget.getRoutingOptions());
        if (routingOptions.equals(TrafficRoutingOptions.SINGLE_TARGET)) {
            SwitchRecordTargetContext<EdsConfigs.Aliyun, DescribeDomainRecordsResponseBody.Record> context = buildSwitchContext(
                    switchRecordTarget);
            handleSingleTargetRouting(context);
        } else {
            TrafficRouteException.runtime("Current operation not implemented.");
        }
    }

    @Override
    public String generateConsoleURL(TrafficRoute trafficRoute) {
        String rr = SwitchRecordTargetContext.getRR(trafficRoute.getDomain(), trafficRoute.getDomainRecord());
        return StringFormatter.arrayFormat(CONSOLE_URL, trafficRoute.getDomain(), rr);
    }

    @Override
    protected List<DescribeDomainRecordsResponseBody.Record> queryTrafficRouteRecords(EdsConfigs.Aliyun config,
                                                                                      TrafficRoute trafficRoute) {
        List<DescribeDomainRecordsResponseBody.Record> records = AliyunDnsRepo.describeDomainRecords(
                config, trafficRoute.getDomain());
        if (CollectionUtils.isEmpty(records)) {
            return List.of();
        }
        String domainRecord = trafficRoute.getDomainRecord();
        return records.stream()
                .filter(record -> isCnameOrARecord(record.getType()) && domainRecord.equals(
                        buildFullRecordName(record)))
                .toList();
    }

    @Override
    protected void handleSingleTargetRouting(
            SwitchRecordTargetContext<EdsConfigs.Aliyun, DescribeDomainRecordsResponseBody.Record> context) {
        // 删除非当前路由类型的解析，当前是CNAME则删除所有A，当前是A则删除所有的CNAME
        DnsRRType dnsRRType = context.getDnsRRType();
        DnsRRType conflictingDnsRRType = context.getConflictingDnsRRType();
        // 删除所有冲突解析
        List<DescribeDomainRecordsResponseBody.Record> conflictingMatchedRecords = context.getMatchedRecordMap()
                .get(conflictingDnsRRType.name());
        if (!CollectionUtils.isEmpty(conflictingMatchedRecords)) {
            for (DescribeDomainRecordsResponseBody.Record conflictingMatchedRecord : conflictingMatchedRecords) {
                AliyunDnsRepo.deleteDomainRecord(context.getConfig(), conflictingMatchedRecord.getRecordId());
            }
        }

        List<DescribeDomainRecordsResponseBody.Record> records = context.getMatchedRecordMap()
                .get(context.getDnsRRType()
                             .name());
        // 没有记录则新增
        if (CollectionUtils.isEmpty(records)) {
            addNewRecord(context);
            return;
        }

        if (CollectionUtils.isEmpty(context.getMatchedRecordMap()
                                            .get(dnsRRType.name()))) {
            addNewRecord(context);
            return;
        }
        if (records.size() == 1) {
            updateSingleRecord(context, records.getFirst());
            return;
        }
        handleMultipleRecords(context);
    }

    private void addNewRecord(
            SwitchRecordTargetContext<EdsConfigs.Aliyun, DescribeDomainRecordsResponseBody.Record> context) {
        AliyunDnsRepo.addDomainRecord(
                context.getConfig(), context.getDomain(), context.getRR(), context.getDnsRRType()
                        .name(), context.getRecordValue(), context.getTrafficRecordTarget()
                        .getTtl()
        );
    }

    private void updateSingleRecord(
            SwitchRecordTargetContext<EdsConfigs.Aliyun, DescribeDomainRecordsResponseBody.Record> context,
            DescribeDomainRecordsResponseBody.Record record) {
        if (!context.getDnsRRType()
                .name()
                .equals(record.getType()) || !record.getValue()
                .equals(context.getRecordValue())) {
            AliyunDnsRepo.updateDomainRecord(
                    context.getConfig(), record.getRecordId(), context.getRR(), context.getDnsRRType()
                            .name(), context.getRecordValue(), context.getTTL()
            );
        } else {
            TrafficRouteException.runtime("DNS record already exists, no operation performed.");
        }
    }

    /**
     * 路由模式修改
     *
     * @param context
     */
    private void handleMultipleRecords(
            SwitchRecordTargetContext<EdsConfigs.Aliyun, DescribeDomainRecordsResponseBody.Record> context) {
        List<DescribeDomainRecordsResponseBody.Record> records = context.getMatchedRecordMap()
                .get(context.getDnsRRType()
                             .name());
        Optional<DescribeDomainRecordsResponseBody.Record> optionalRecord = records.stream()
                .filter(e -> e.getValue()
                        .equals(context.getRecordValue()))
                .findFirst();
        if (optionalRecord.isEmpty()) {
            addNewRecord(context);
        }
        context.getMatchedRecords()
                .stream()
                .map(DescribeDomainRecordsResponseBody.Record::getRecordId)
                .filter(id -> optionalRecord.isPresent() && !id.equals(optionalRecord.get()
                                                                               .getRecordId()))
                .forEach(id -> AliyunDnsRepo.deleteDomainRecord(context.getConfig(), id));
    }

    private String buildFullRecordName(DescribeDomainRecordsResponseBody.Record record) {
        return record.getRr() + "." + record.getDomainName();
    }

    @Override
    protected DNS.ResourceRecordSet findMatchedRecord(List<DescribeDomainRecordsResponseBody.Record> records,
                                                      TrafficRoute trafficRoute) {
        String domainRecord = trafficRoute.getDomainRecord();
        List<DescribeDomainRecordsResponseBody.Record> matchedRecords = records.stream()
                .filter(record -> isCnameOrARecord(record.getType()) && domainRecord.equals(
                        buildFullRecordName(record)))
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

    private List<DNS.ResourceRecord> toResourceRecords(List<DescribeDomainRecordsResponseBody.Record> records) {
        return records.stream()
                .map(record -> DNS.ResourceRecord.builder()
                        .weight(record.getWeight() == null ? null : Long.valueOf(record.getWeight()))
                        .value(record.getValue())
                        .build())
                .toList();
    }

}
