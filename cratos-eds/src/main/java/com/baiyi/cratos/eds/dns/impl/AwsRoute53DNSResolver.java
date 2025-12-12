package com.baiyi.cratos.eds.dns.impl;

import com.amazonaws.services.route53.model.HostedZone;
import com.amazonaws.services.route53.model.ResourceRecordSet;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.generator.TrafficRoute;
import com.baiyi.cratos.eds.aws.repo.AwsRoute53Repo;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsAwsConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.dns.BaseDNSResolver;
import com.baiyi.cratos.eds.dns.model.DNS;
import com.baiyi.cratos.service.EdsAssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2025/12/11 16:16
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.AWS)
public class AwsRoute53DNSResolver extends BaseDNSResolver {

    private final EdsAssetService edsAssetService;
    private final EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder;

    @Override
    public DNS.ResourceRecordSet getDNSResourceRecordSet(TrafficRoute trafficRoute) {
        // 获取托管区域
        String hostedZoneId = getHostedZoneId(trafficRoute);
        if (hostedZoneId == null) {
            return DNS.ResourceRecordSet.NO_DATA;
        }
        // 获取 AWS 配置
        EdsAwsConfigModel.Aws config = getAwsConfig(trafficRoute);
        // 查询 DNS 记录
        List<ResourceRecordSet> resourceRecordSets = AwsRoute53Repo.listResourceRecordSets(config, hostedZoneId);
        // 查找匹配的记录
        return findMatchingRecord(resourceRecordSets, trafficRoute);
    }

    private String getHostedZoneId(TrafficRoute trafficRoute) {
        String domainFqdn = toFQDN(trafficRoute.getDomain());
        List<EdsAsset> hostedZoneAssets = edsAssetService.queryInstanceAssetByTypeAndName(
                trafficRoute.getDnsResolverInstanceId(), EdsAssetTypeEnum.AWS_HOSTED_ZONE.name(), domainFqdn, false);
        return CollectionUtils.isEmpty(hostedZoneAssets) ? null : hostedZoneAssets.getFirst()
                .getAssetId();
    }

    @SuppressWarnings("unchecked")
    private EdsAwsConfigModel.Aws getAwsConfig(TrafficRoute trafficRoute) {
        EdsInstanceProviderHolder<EdsAwsConfigModel.Aws, HostedZone> holder = (EdsInstanceProviderHolder<EdsAwsConfigModel.Aws, HostedZone>) edsInstanceProviderHolderBuilder.newHolder(
                trafficRoute.getDnsResolverInstanceId(), EdsAssetTypeEnum.AWS_HOSTED_ZONE.name());
        return holder.getInstance()
                .getConfig();
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
                .tTL(record.getTTL())
                .weight(record.getWeight())
                .resourceRecords(record.getResourceRecords()
                                         .stream()
                                         .map(r -> DNS.ResourceRecord.builder()
                                                 .value(r.getValue())
                                                 .build())
                                         .toList())
                .build();
    }

}
