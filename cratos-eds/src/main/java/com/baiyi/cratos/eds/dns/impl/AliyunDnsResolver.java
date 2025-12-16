package com.baiyi.cratos.eds.dns.impl;

import com.aliyun.sdk.service.alidns20150109.models.DescribeDomainRecordsResponseBody;
import com.baiyi.cratos.common.enums.TrafficRoutingOptions;
import com.baiyi.cratos.common.exception.TrafficRouteException;
import com.baiyi.cratos.domain.generator.TrafficRecordTarget;
import com.baiyi.cratos.domain.generator.TrafficRoute;
import com.baiyi.cratos.domain.model.DNS;
import com.baiyi.cratos.domain.param.http.traffic.TrafficRouteParam;
import com.baiyi.cratos.domain.util.dnsgoogle.enums.DnsTypes;
import com.baiyi.cratos.eds.aliyun.repo.AliyunDnsRepo;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.dns.BaseDNSResolver;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.TrafficRecordTargetService;
import com.baiyi.cratos.service.TrafficRouteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2025/12/12 10:19
 * @Version 1.0
 */
@Slf4j
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.ALIYUN)
public class AliyunDnsResolver extends BaseDNSResolver<EdsConfigs.Aliyun, DescribeDomainRecordsResponseBody.Record> {

    public AliyunDnsResolver(EdsAssetService edsAssetService, TrafficRouteService trafficRouteService,
                             TrafficRecordTargetService trafficRecordTargetService,
                             EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder) {
        super(edsAssetService, trafficRouteService, trafficRecordTargetService, edsInstanceProviderHolderBuilder);
    }

    @Override
    public DNS.ResourceRecordSet getDNSResourceRecordSet(TrafficRoute trafficRoute) {
        // 获取阿里云配置
        EdsConfigs.Aliyun config = getEdsConfig(trafficRoute, EdsAssetTypeEnum.ALIYUN_DOMAIN);
        // 查询 DNS 记录
        List<DescribeDomainRecordsResponseBody.Record> records = AliyunDnsRepo.describeDomainRecords(
                config, trafficRoute.getDomain());
        // 查找匹配的记录
        return findMatchedRecord(records, trafficRoute);
    }

    @Override
    public void switchToRoute(TrafficRouteParam.SwitchRecordTarget switchRecordTarget) {
        TrafficRecordTarget trafficRecordTarget = getTrafficRecordTargetById(switchRecordTarget.getRecordTargetId());
        TrafficRoute trafficRoute = getTrafficRouteById(trafficRecordTarget.getTrafficRouteId());
        EdsConfigs.Aliyun config = getEdsConfig(trafficRoute, EdsAssetTypeEnum.ALIYUN_DOMAIN);
        List<DescribeDomainRecordsResponseBody.Record> matchedRecords = getTrafficRouteRecords(config, trafficRoute);
        validateRecordCount(matchedRecords);
        TrafficRoutingOptions routingOptions = TrafficRoutingOptions.valueOf(switchRecordTarget.getRoutingOptions());
        if (routingOptions.equals(TrafficRoutingOptions.SINGLE_TARGET)) {
            handleSingleTargetRouting(config, trafficRoute, trafficRecordTarget, matchedRecords);
        } else {
            TrafficRouteException.runtime("Current operation not implemented");
        }
    }

    @Override
    protected List<DescribeDomainRecordsResponseBody.Record> getTrafficRouteRecords(EdsConfigs.Aliyun aliyun,
                                                                                    TrafficRoute trafficRoute) {
        List<DescribeDomainRecordsResponseBody.Record> records = AliyunDnsRepo.describeDomainRecords(
                aliyun, trafficRoute.getDomain());
        String recordType = trafficRoute.getRecordType();
        String domainRecord = trafficRoute.getDomainRecord();
        return records.stream()
                .filter(record -> recordType.equals(record.getType()) && domainRecord.equals(
                        buildFullRecordName(record)))
                .toList();
    }

    private void handleSingleTargetRouting(EdsConfigs.Aliyun aliyun, TrafficRoute trafficRoute,
                                           TrafficRecordTarget trafficRecordTarget,
                                           List<DescribeDomainRecordsResponseBody.Record> matchedRecords) {
        DnsTypes dnsType = DnsTypes.valueOf(trafficRecordTarget.getRecordType());

        if (CollectionUtils.isEmpty(matchedRecords)) {
            addNewRecord(aliyun, trafficRoute, trafficRecordTarget, dnsType);
        } else if (matchedRecords.size() == 1) {
            updateSingleRecord(aliyun, trafficRoute, trafficRecordTarget, matchedRecords.getFirst(), dnsType);
        } else {
            handleMultipleRecords(aliyun, trafficRoute, trafficRecordTarget, matchedRecords, dnsType);
        }
    }

    private void addNewRecord(EdsConfigs.Aliyun aliyun, TrafficRoute trafficRoute,
                              TrafficRecordTarget trafficRecordTarget, DnsTypes dnsType) {
        AliyunDnsRepo.addDomainRecord(
                aliyun, trafficRoute.getDomain(), getRR(trafficRoute), dnsType.name(),
                trafficRecordTarget.getRecordValue(), trafficRecordTarget.getTtl()
        );
    }

    private void updateSingleRecord(EdsConfigs.Aliyun aliyun, TrafficRoute trafficRoute,
                                    TrafficRecordTarget trafficRecordTarget,
                                    DescribeDomainRecordsResponseBody.Record record, DnsTypes dnsType) {
        String recordType = trafficRecordTarget.getRecordType();
        if (!recordType.equals(record.getType()) || !record.getValue()
                .equals(trafficRecordTarget.getRecordValue())) {
            AliyunDnsRepo.updateDomainRecord(
                    aliyun, record.getRecordId(), getRR(trafficRoute), dnsType.name(),
                    trafficRecordTarget.getRecordValue(), trafficRecordTarget.getTtl()
            );
        } else {
            TrafficRouteException.runtime("DNS record already exists, no operation performed");
        }
    }

    private void handleMultipleRecords(EdsConfigs.Aliyun aliyun, TrafficRoute trafficRoute,
                                       TrafficRecordTarget trafficRecordTarget,
                                       List<DescribeDomainRecordsResponseBody.Record> matchedRecords,
                                       DnsTypes dnsType) {
        Optional<DescribeDomainRecordsResponseBody.Record> optionalRecord = matchedRecords.stream()
                .filter(e -> e.getValue()
                        .equals(trafficRecordTarget.getRecordValue()))
                .findFirst();

        if (optionalRecord.isPresent()) {
            updateAndDeleteOthers(
                    aliyun, trafficRoute, trafficRecordTarget, matchedRecords, optionalRecord.get(), dnsType);
        } else {
            addAndDeleteAll(aliyun, trafficRoute, trafficRecordTarget, matchedRecords, dnsType);
        }
    }

    private void updateAndDeleteOthers(EdsConfigs.Aliyun aliyun, TrafficRoute trafficRoute,
                                       TrafficRecordTarget trafficRecordTarget,
                                       List<DescribeDomainRecordsResponseBody.Record> matchedRecords,
                                       DescribeDomainRecordsResponseBody.Record targetRecord, DnsTypes dnsType) {
        final String recordId = targetRecord.getRecordId();
        AliyunDnsRepo.updateDomainRecord(
                aliyun, recordId, getRR(trafficRoute), dnsType.name(),
                trafficRecordTarget.getRecordValue(), trafficRecordTarget.getTtl()
        );

        matchedRecords.forEach(record -> {
            if (!record.getRecordId()
                    .equals(recordId)) {
                AliyunDnsRepo.deleteDomainRecord(aliyun, record.getRecordId());
            }
        });
    }

    private void addAndDeleteAll(EdsConfigs.Aliyun aliyun, TrafficRoute trafficRoute,
                                 TrafficRecordTarget trafficRecordTarget,
                                 List<DescribeDomainRecordsResponseBody.Record> matchedRecords, DnsTypes dnsType) {
        AliyunDnsRepo.addDomainRecord(
                aliyun, trafficRoute.getDomain(), getRR(trafficRoute), dnsType.name(),
                trafficRecordTarget.getRecordValue(), trafficRecordTarget.getTtl()
        );

        matchedRecords.forEach(record -> AliyunDnsRepo.deleteDomainRecord(aliyun, record.getRecordId()));
    }

    private String buildFullRecordName(DescribeDomainRecordsResponseBody.Record record) {
        return record.getRr() + "." + record.getDomainName();
    }

    private DNS.ResourceRecordSet findMatchedRecord(List<DescribeDomainRecordsResponseBody.Record> records,
                                                    TrafficRoute trafficRoute) {
        String recordType = trafficRoute.getRecordType();
        String domainRecord = trafficRoute.getDomainRecord();
        List<DescribeDomainRecordsResponseBody.Record> matchedRecords = records.stream()
                .filter(record -> recordType.equals(record.getType()) && domainRecord.equals(
                        buildFullRecordName(record)))
                .toList();
        if (CollectionUtils.isEmpty(matchedRecords)) {
            return DNS.ResourceRecordSet.NO_DATA;
        }
        return DNS.ResourceRecordSet.builder()
                .type(recordType)
                .name(domainRecord)
                .resourceRecords(toResourceRecords(records))
                .build();
    }

    private List<DNS.ResourceRecord> toResourceRecords(List<DescribeDomainRecordsResponseBody.Record> records) {
        return records.stream()
                .map(record -> DNS.ResourceRecord.builder()
                        .weight(Long.valueOf(record.getWeight()))
                        .value(record.getValue())
                        .build())
                .toList();
    }

}
