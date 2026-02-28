package com.baiyi.cratos.eds.acme.dns;

import com.baiyi.cratos.domain.HasZone;
import com.baiyi.cratos.domain.generator.AcmeDomain;
import com.baiyi.cratos.eds.core.config.base.HasEdsConfig;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.dnsgoogle.enums.DnsRRType;
import com.baiyi.cratos.service.EdsAssetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
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
    protected final EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder;

    @SuppressWarnings("unchecked")
    protected Config getEdsConfig(HasZone hasZone) {
        EdsInstanceProviderHolder<Config, ?> holder = (EdsInstanceProviderHolder<Config, ?>) edsInstanceProviderHolderBuilder.newHolder(
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

    abstract protected List<Record> findMatchedRecord(AcmeDomain acmeDomain, List<Record> records);

    @Override
    public void recoverDcvDelegation(AcmeDomain acmeDomain) {
        // 删除 ACME Challenge 记录
        deleteAcmeChallenge(acmeDomain);
        if (!StringUtils.hasText(acmeDomain.getDcvType())) {
            log.info("No DCV delegation configured for domain: {}", acmeDomain.getDomain());
            return;
        }
        // 构造 DCV CNAME 记录值
        // 格式: _acme-challenge.example.com CNAME example.com.{dcv_delegation_target}
        String dcvRecordValue = acmeDomain.getDomain() + "." + acmeDomain.getDcvDelegationTarget();
        log.info("Recovering DCV delegation for domain: {}, type: {}, target: {}",
                 acmeDomain.getDomain(), acmeDomain.getDcvType(), dcvRecordValue);
        addDcvChallengeRecord(acmeDomain, dcvRecordValue);
        log.info("DCV delegation recovered successfully for domain: {}", acmeDomain.getDomain());
    }

    abstract protected void addDcvChallengeRecord(AcmeDomain acmeDomain, String dcvRecordValue);

}
