package com.baiyi.cratos.eds.acme.impl;

import com.amazonaws.services.route53.model.*;
import com.baiyi.cratos.common.exception.EdsAcmeException;
import com.baiyi.cratos.domain.generator.AcmeDomain;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.model.AcmeDNS;
import com.baiyi.cratos.eds.acme.BaseAcmeDNSResolver;
import com.baiyi.cratos.eds.aws.repo.AwsRoute53Repo;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.annotation.ToFQDN;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.service.EdsAssetService;
import com.google.api.client.util.Lists;
import com.google.common.collect.Maps;
import org.shredzone.acme4j.Authorization;
import org.shredzone.acme4j.Order;
import org.shredzone.acme4j.challenge.Dns01Challenge;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/2/12 14:43
 * &#064;Version 1.0
 */
@Component
@ToFQDN
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.AWS, assetTypeOf = EdsAssetTypeEnum.AWS_HOSTED_ZONE)
public class AcmeAwsRoute53Resolver extends BaseAcmeDNSResolver<EdsConfigs.Aws, ResourceRecordSet> {

    private static final String CONSOLE_URL = "https://us-east-1.console.aws.amazon.com/route53/v2/hostedzones?region=eu-west-1#ListRecordSets/{}";

    public AcmeAwsRoute53Resolver(EdsAssetService edsAssetService,
                                  EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder) {
        super(edsAssetService, edsInstanceProviderHolderBuilder);
    }

    @Override
    public AcmeDNS.AcmeChallengeRecord getAcmeChallenge(AcmeDomain acmeDomain) {
        // 获取托管区域
        final String hostedZoneId = getZoneId(acmeDomain);
        if (!StringUtils.hasText(hostedZoneId)) {
            return AcmeDNS.AcmeChallengeRecord.NO_DATA;
        }
        // 获取 AWS 配置
        EdsConfigs.Aws config = getEdsConfig(acmeDomain);
        // 查询 DNS 记录
        List<ResourceRecordSet> resourceRecordSets = AwsRoute53Repo.listResourceRecordSets(config, hostedZoneId);
        // 查找匹配的记录
        return findMatchedRecord(resourceRecordSets, acmeDomain);
    }

    @Override
    public void deleteAcmeChallenge(AcmeDomain acmeDomain) {
        // 获取托管区域
        final String hostedZoneId = getZoneId(acmeDomain);
        if (!StringUtils.hasText(hostedZoneId)) {
            return;
        }
        // 获取 AWS 配置
        EdsConfigs.Aws config = getEdsConfig(acmeDomain);
        // 查询 DNS 记录
        final String acmeChallengeRecordFqdn = toFQDN(ACME_CHALLENGE_NAME + "." + acmeDomain.getDomain());
        List<ResourceRecordSet> resourceRecordSets = AwsRoute53Repo.listResourceRecordSets(config, hostedZoneId)
                .stream()
                .filter(record -> isCnameOrARecord(record.getType()) && acmeChallengeRecordFqdn.equals(
                        record.getName()))
                .toList();
        if (CollectionUtils.isEmpty(resourceRecordSets)) {
            // 无记录
            return;
        }
        if (resourceRecordSets.size() > 1) {
            EdsAcmeException.runtime("查询到的ACME记录 _acme-challenge 超过1条");
        }
        List<Change> changes = resourceRecordSets.stream()
                .map(e -> new Change(ChangeAction.DELETE, e))
                .toList();
        AwsRoute53Repo.changeResourceRecordSets(config, hostedZoneId, changes);
    }

    @Override
    public void addOrderChallengeRecords(AcmeDomain acmeDomain, Order order) {
        // 获取托管区域
        final String hostedZoneId = getZoneId(acmeDomain);
        if (!StringUtils.hasText(hostedZoneId)) {
            return;
        }
        // 获取 AWS 配置
        EdsConfigs.Aws config = getEdsConfig(acmeDomain);
        Map<String, List<String>> dnsRecords = Maps.newHashMap();
        for (Authorization auth : order.getAuthorizations()) {
            Optional<Dns01Challenge> optionalDnsChallenge = auth.findChallenge(Dns01Challenge.class);
            if (optionalDnsChallenge.isPresent()) {
                Dns01Challenge dns01Challenge = optionalDnsChallenge.get();
                String domain = auth.getIdentifier()
                        .getDomain();
                String recordName = "_acme-challenge." + domain.replace("*.", "");
                if (dnsRecords.containsKey(recordName)) {
                    dnsRecords.get(recordName)
                            .add(dns01Challenge.getDigest());
                } else {
                    List<String> recordValues = Lists.newArrayList();
                    recordValues.add(dns01Challenge.getDigest());
                    dnsRecords.put(recordName, recordValues);
                }
            }
        }
        // 创建 DNS 记录
        dnsRecords.forEach((recordName, recordValues) -> {
            ResourceRecordSet rrs = new ResourceRecordSet(toFQDN(recordName), RRType.TXT);
            rrs.setTTL(300L);
            rrs.setResourceRecords(recordValues.stream()
                                           .map(v -> new ResourceRecord("\"" + v + "\""))
                                           .toList());
            Change upsertChange = new Change(ChangeAction.UPSERT, rrs);
            AwsRoute53Repo.changeResourceRecordSets(config, hostedZoneId, List.of(upsertChange));
        });
    }

    protected AcmeDNS.AcmeChallengeRecord findMatchedRecord(List<ResourceRecordSet> resourceRecordSets,
                                                            AcmeDomain acmeDomain) {
        final String acmeChallengeRecordFqdn = toFQDN(ACME_CHALLENGE_NAME + "." + acmeDomain.getDomain());
        return resourceRecordSets.stream()
                .filter(record -> isCnameOrARecord(record.getType()) && acmeChallengeRecordFqdn.equals(
                        record.getName()))
                .map(this::convertToAcmeChallengeRecord)
                .findFirst()
                .orElse(AcmeDNS.AcmeChallengeRecord.NO_DATA);
    }

    private AcmeDNS.AcmeChallengeRecord convertToAcmeChallengeRecord(ResourceRecordSet record) {
        ResourceRecord resourceRecord = record.getResourceRecords()
                .getFirst();
        return AcmeDNS.AcmeChallengeRecord.builder()
                .type(record.getType())
                // 移除末尾的点
                .name(removeFQDNRoot(record.getName()))
                .value(resourceRecord.getValue())
                .build();
    }

    @Override
    public String getZoneId(AcmeDomain acmeDomain) {
        if (StringUtils.hasText(acmeDomain.getZoneId())) {
            return acmeDomain.getZoneId();
        }
        String domainFqdn = toFQDN(acmeDomain.getDomain());
        List<EdsAsset> hostedZoneAssets = edsAssetService.queryInstanceAssetByTypeAndName(
                acmeDomain.getDnsResolverInstanceId(), EdsAssetTypeEnum.AWS_HOSTED_ZONE.name(), domainFqdn, false);
        return CollectionUtils.isEmpty(hostedZoneAssets) ? null : hostedZoneAssets.getFirst()
                .getAssetId();
    }

}