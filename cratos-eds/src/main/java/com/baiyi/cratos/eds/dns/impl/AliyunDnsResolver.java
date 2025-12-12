package com.baiyi.cratos.eds.dns.impl;

import com.aliyun.sdk.service.alidns20150109.models.DescribeDomainRecordsResponseBody;
import com.baiyi.cratos.domain.generator.TrafficRoute;
import com.baiyi.cratos.eds.aliyun.repo.AliyunDnsRepo;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsAliyunConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.dns.BaseDNSResolver;
import com.baiyi.cratos.domain.model.DNS;
import com.baiyi.cratos.service.EdsAssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2025/12/12 10:19
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.ALIYUN)
public class AliyunDnsResolver extends BaseDNSResolver {

    private final EdsAssetService edsAssetService;
    private final EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder;

    @Override
    public DNS.ResourceRecordSet getDNSResourceRecordSet(TrafficRoute trafficRoute) {
        // 获取阿里云配置
        EdsAliyunConfigModel.Aliyun aliyun = getAliyunConfig(trafficRoute);
        // 查询 DNS 记录
        List<DescribeDomainRecordsResponseBody.Record> records = AliyunDnsRepo.describeDomainRecords(
                aliyun, trafficRoute.getDomain());
        // 查找匹配的记录
        return findMatchedRecord(records, trafficRoute);
    }

    @SuppressWarnings("unchecked")
    private EdsAliyunConfigModel.Aliyun getAliyunConfig(TrafficRoute trafficRoute) {
        EdsInstanceProviderHolder<EdsAliyunConfigModel.Aliyun, ?> holder = (EdsInstanceProviderHolder<EdsAliyunConfigModel.Aliyun, ?>) edsInstanceProviderHolderBuilder.newHolder(
                trafficRoute.getDnsResolverInstanceId(), EdsAssetTypeEnum.ALIYUN_DOMAIN.name());
        return holder.getInstance()
                .getConfig();
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
