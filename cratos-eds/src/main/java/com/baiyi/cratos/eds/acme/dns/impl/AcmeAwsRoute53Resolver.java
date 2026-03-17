package com.baiyi.cratos.eds.acme.dns.impl;

import com.amazonaws.services.route53.model.*;
import com.baiyi.cratos.common.exception.EdsAcmeException;
import com.baiyi.cratos.domain.generator.AcmeDomain;
import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.eds.acme.dns.BaseAcmeDNSResolver;
import com.baiyi.cratos.eds.acme.model.AcmeDnsRecord;
import com.baiyi.cratos.eds.aws.repo.AwsRoute53Repo;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.annotation.ToFQDN;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsProviderHolderFactory;
import com.baiyi.cratos.service.EdsAssetService;
import org.shredzone.acme4j.Order;
import org.shredzone.acme4j.challenge.Dns01Challenge;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/2/12 14:43
 * &#064;Version 1.0
 */
@Component
@ToFQDN
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.AWS, assetTypeOf = EdsAssetTypeEnum.AWS_HOSTED_ZONE)
public class AcmeAwsRoute53Resolver extends BaseAcmeDNSResolver<EdsConfigs.Aws, ResourceRecordSet> {

    public AcmeAwsRoute53Resolver(EdsAssetService edsAssetService,
                                  EdsProviderHolderFactory edsProviderHolderFactory) {
        super(edsAssetService, edsProviderHolderFactory);
    }

    @Override
    public void deleteAcmeChallenge(AcmeDomain acmeDomain,Order order) {
        // 获取托管区域
        final String hostedZoneId = getZoneId(acmeDomain);
        if (!StringUtils.hasText(hostedZoneId)) {
            return;
        }
        // 获取 AWS 配置
        EdsConfigs.Aws config = getEdsConfig(acmeDomain);
        // ACME 记录
        List<AcmeDnsRecord> acmeDnsRecords = getAcmeDnsRecords(order);
        // 查询 DNS 记录
        List<ResourceRecordSet> resourceRecordSets = findMatchedRecord(
                acmeDnsRecords, AwsRoute53Repo.listResourceRecordSets(config, hostedZoneId));
        if (CollectionUtils.isEmpty(resourceRecordSets)) {
            // 无记录
            return;
        }
        if (resourceRecordSets.size() > 2) {
            EdsAcmeException.runtime("查询到的ACME记录 _acme-challenge 超过2条");
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
        // 提取所有 DNS Challenge 的 digest 值
        List<String> recordValues = order.getAuthorizations()
                .stream()
                .map(auth -> auth.findChallenge(Dns01Challenge.class))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(Dns01Challenge::getDigest)
                .toList();
        if (recordValues.isEmpty()) {
            return;
        }
        // 构建记录名（去掉通配符）
        String recordName = ACME_CHALLENGE_NAME + "." + acmeDomain.getDomain()
                .replace("*.", "");
        ResourceRecordSet rrs = new ResourceRecordSet(toFQDN(recordName), RRType.TXT);
        rrs.setTTL(300L);
        rrs.setResourceRecords(recordValues.stream()
                                       .map(v -> new ResourceRecord("\"" + v + "\""))
                                       .toList());
        Change upsertChange = new Change(ChangeAction.UPSERT, rrs);
        AwsRoute53Repo.changeResourceRecordSets(config, hostedZoneId, List.of(upsertChange));
    }

    @Override
    protected void addDcvChallengeRecord(AcmeDomain acmeDomain, String dcvRecordValue) {
        // 获取 AWS 配置
        EdsConfigs.Aws config = getEdsConfig(acmeDomain);
        final String hostedZoneId = getZoneId(acmeDomain);
        
        String recordName = ACME_CHALLENGE_NAME + "." + acmeDomain.getDomain();
        ResourceRecordSet rrs = new ResourceRecordSet(toFQDN(recordName), RRType.CNAME);
        rrs.setTTL(300L);
        rrs.setResourceRecords(List.of(new ResourceRecord(dcvRecordValue)));
        
        Change upsertChange = new Change(ChangeAction.UPSERT, rrs);
        AwsRoute53Repo.changeResourceRecordSets(config, hostedZoneId, List.of(upsertChange));
    }

    @Override
    protected List<ResourceRecordSet> findMatchedRecord(List<AcmeDnsRecord> acmeDnsRecords,
                                                        List<ResourceRecordSet> resourceRecordSets) {
        Set<String> acmeChallengeRecords =acmeDnsRecords.stream()
                .map(e -> toFQDN(e.getRecordName() + "." + e.getDomain()))
                .collect(Collectors.toSet());
        return resourceRecordSets.stream()
                .filter(record -> isCnameOrTxtRecord(record.getType()) && acmeChallengeRecords.contains(record.getName()))
                .toList();
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