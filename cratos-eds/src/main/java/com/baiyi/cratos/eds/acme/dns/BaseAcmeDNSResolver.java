package com.baiyi.cratos.eds.acme.dns;

import com.baiyi.cratos.domain.HasZone;
import com.baiyi.cratos.domain.generator.AcmeDomain;
import com.baiyi.cratos.eds.acme.model.AcmeDnsRecord;
import com.baiyi.cratos.eds.core.config.base.HasEdsConfig;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsProviderHolderFactory;
import com.baiyi.cratos.eds.dnsgoogle.enums.DnsRRType;
import com.baiyi.cratos.service.EdsAssetService;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shredzone.acme4j.Authorization;
import org.shredzone.acme4j.Order;
import org.shredzone.acme4j.challenge.Dns01Challenge;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/2/12 14:32
 * &#064;Version 1.0
 */
@Slf4j
@RequiredArgsConstructor
public abstract class BaseAcmeDNSResolver<Config extends HasEdsConfig, Record> implements AcmeDNSResolver {

    public static final String ACME_CHALLENGE_NAME = "_acme-challenge";

    protected final EdsAssetService edsAssetService;
    protected final EdsProviderHolderFactory edsProviderHolderFactory;

    @SuppressWarnings("unchecked")
    protected Config getEdsConfig(HasZone hasZone) {
        EdsInstanceProviderHolder<Config, ?> holder = (EdsInstanceProviderHolder<Config, ?>) edsProviderHolderFactory.createHolder(
                hasZone.getDnsResolverInstanceId(), getAssetTypeEnum().name());
        return holder.getInstance()
                .getConfig();
    }

    protected boolean isCnameOrTxtRecord(String recordType) {
        return DnsRRType.CNAME.name()
                .equals(recordType) || DnsRRType.TXT.name()
                .equals(recordType);
    }

    /**
     * example.com --> example.com.
     *
     * @param domainName
     * @return
     */
    protected String toFQDN(String domainName) {
        if (domainName == null || domainName.isEmpty()) {
            return domainName;
        }
        return domainName.endsWith(".") ? domainName : domainName + ".";
    }

    protected Set<String> toDomains(AcmeDomain acmeDomain) {
        return Arrays.stream(acmeDomain.getDomains()
                                     .split(","))
                .map(s -> s.replace("*.", ""))
                .collect(Collectors.toSet());
    }

    /**
     * example.com. --> example.com
     *
     * @param fQDNOrDomain
     * @return
     */
    protected String removeFQDNRoot(String fQDNOrDomain) {
        return fQDNOrDomain.replaceAll("\\.$", "");
    }

    abstract protected List<Record> findMatchedRecord(List<AcmeDnsRecord> acmeDnsRecords, List<Record> records);

    // abstract protected List<Record> findMatchedRecord(AcmeDomain acmeDomain, List<Record> records);

    protected List<AcmeDnsRecord> getAcmeDnsRecords(Order order) {
        List<AcmeDnsRecord> records = Lists.newArrayList();
        for (Authorization authorization : order.getAuthorizations()) {
            Optional<Dns01Challenge> optionalChallenge = authorization.findChallenge(Dns01Challenge.class);
            optionalChallenge.ifPresent(dns01Challenge -> records.add(AcmeDnsRecord.builder()
                                                                              .domain(authorization.getIdentifier()
                                                                                              .getDomain())
                                                                              .digest(dns01Challenge.getDigest())
                                                                              .build()));
        }
        return records;
    }

    @Override
    public void recoverDcvDelegation(AcmeDomain acmeDomain) {
        // 删除 ACME Challenge 记录
        if (!StringUtils.hasText(acmeDomain.getDcvType())) {
            log.info("No DCV delegation configured for domain: {}", acmeDomain.getDomain());
            return;
        }
        // 构造 DCV CNAME 记录值
        // 格式: _acme-challenge.example.com CNAME example.com.{dcv_delegation_target}
        String dcvRecordValue = acmeDomain.getDomain() + "." + acmeDomain.getDcvDelegationTarget();
        log.info(
                "Recovering DCV delegation for domain: {}, type: {}, target: {}", acmeDomain.getDomain(),
                acmeDomain.getDcvType(), dcvRecordValue
        );
        addDcvChallengeRecord(acmeDomain, dcvRecordValue);
        log.info("DCV delegation recovered successfully for domain: {}", acmeDomain.getDomain());
    }

    abstract protected void addDcvChallengeRecord(AcmeDomain acmeDomain, String dcvRecordValue);

}
