package com.baiyi.cratos.eds.acme.dns.impl;

import com.aliyun.sdk.service.alidns20150109.models.DescribeDomainRecordsResponseBody;
import com.baiyi.cratos.common.exception.EdsAcmeException;
import com.baiyi.cratos.domain.generator.AcmeDomain;
import com.baiyi.cratos.eds.acme.dns.BaseAcmeDNSResolver;
import com.baiyi.cratos.eds.acme.model.AcmeDnsRecord;
import com.baiyi.cratos.eds.aliyun.repo.AliyunDnsRepo;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsProviderHolderFactory;
import com.baiyi.cratos.eds.dnsgoogle.enums.DnsRRType;
import com.baiyi.cratos.service.EdsAssetService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.shredzone.acme4j.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/2/12 14:32
 * &#064;Version 1.0
 */
@Slf4j
@Component
@EdsInstanceAssetType(instanceTypeOf = EdsInstanceTypeEnum.ALIYUN, assetTypeOf = EdsAssetTypeEnum.ALIYUN_DOMAIN)
public class AcmeAliyunDNSResolver extends BaseAcmeDNSResolver<EdsConfigs.Aliyun, DescribeDomainRecordsResponseBody.Record> {

    public AcmeAliyunDNSResolver(EdsAssetService edsAssetService,
                                 EdsProviderHolderFactory edsProviderHolderFactory) {
        super(edsAssetService, edsProviderHolderFactory);
    }

    @Override
    public String getZoneId(AcmeDomain acmeDomain) {
        // 阿里云 DNS 使用域名作为 Zone ID
        return acmeDomain.getDomain();
    }

    @Override
    public void deleteAcmeChallenge(AcmeDomain acmeDomain, Order order) {
        // 获取 Aliyun 配置
        EdsConfigs.Aliyun config = getEdsConfig(acmeDomain);
        // ACME 记录
        List<AcmeDnsRecord> acmeDnsRecords = getAcmeDnsRecords(order);
        // 查询 DNS 记录
        List<DescribeDomainRecordsResponseBody.Record> records = findMatchedRecord(
                acmeDnsRecords, AliyunDnsRepo.describeDomainRecords(
                        config, acmeDomain.getDomain())
        );
        if (CollectionUtils.isEmpty(records)) {
            return;
        }
        if (records.size() > 2) {
            EdsAcmeException.runtime("查询到的ACME记录 _acme-challenge 超过2条");
        }
        // 删除所有匹配的记录
        for (DescribeDomainRecordsResponseBody.Record record : records) {
            AliyunDnsRepo.deleteDomainRecord(config, record.getRecordId());
            log.info("Deleted ACME challenge record: {}", record.getRr());
        }
    }

    @Override
    protected List<DescribeDomainRecordsResponseBody.Record> findMatchedRecord(List<AcmeDnsRecord> acmeDnsRecords,
                                                                               List<DescribeDomainRecordsResponseBody.Record> records) {
        Set<String> acmeChallengeRecords = acmeDnsRecords.stream()
                .map(e -> e.getRecordName() + "." + e.getDomain())
                .collect(Collectors.toSet());
        return records.stream()
                .filter(record -> isCnameOrTxtRecord(record.getType()) && acmeChallengeRecords.contains(
                        buildFullRecordName(record)))
                .toList();
    }

    private String buildFullRecordName(DescribeDomainRecordsResponseBody.Record record) {
        return record.getRr() + "." + record.getDomainName();
    }

    @Override
    public void addOrderChallengeRecords(AcmeDomain acmeDomain, Order order) {
        // 获取 Aliyun 配置
        EdsConfigs.Aliyun config = getEdsConfig(acmeDomain);
        // 提取所有 DNS Challenge 的 digest 值
        List<AcmeDnsRecord> acmeDnsRecords = getAcmeDnsRecords(order);
        if (CollectionUtils.isEmpty(acmeDnsRecords)) {
            return;
        }
        for (AcmeDnsRecord acmeDnsRecord : acmeDnsRecords) {
            String rr;
            if (acmeDomain.getDomain()
                    .equals(acmeDnsRecord.getDomain())) {
                rr = acmeDnsRecord.getRecordName();
            } else {
                rr = acmeDnsRecord.getRecordName() + "." + StringUtils.removeEnd(
                        acmeDnsRecord.getDomain(), "." + acmeDomain.getDomain());
            }
            AliyunDnsRepo.addDomainRecord(
                    config, acmeDomain.getDomain(), rr, DnsRRType.TXT.name(), acmeDnsRecord.getDigest(), 600L);
        }
    }

    @Override
    protected void addDcvChallengeRecord(AcmeDomain acmeDomain, String dcvRecordValue) {
        // 获取 Aliyun 配置
        EdsConfigs.Aliyun config = getEdsConfig(acmeDomain);
        AliyunDnsRepo.addDomainRecord(
                config, acmeDomain.getDomain(), ACME_CHALLENGE_NAME, DnsRRType.CNAME.name(), dcvRecordValue, 600L);
    }

}