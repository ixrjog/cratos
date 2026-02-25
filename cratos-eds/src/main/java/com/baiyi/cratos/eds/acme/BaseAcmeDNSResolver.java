package com.baiyi.cratos.eds.acme;

import com.baiyi.cratos.domain.HasZone;
import com.baiyi.cratos.eds.core.config.base.HasEdsConfig;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.dnsgoogle.enums.DnsRRType;
import com.baiyi.cratos.service.EdsAssetService;
import lombok.RequiredArgsConstructor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/2/12 14:32
 * &#064;Version 1.0
 */
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

    protected boolean isCnameOrARecord(String recordType) {
        return DnsRRType.CNAME.name()
                .equals(recordType) || DnsRRType.A.name()
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

    /**
     * example.com. --> example.com
     *
     * @param fQDNOrDomain
     * @return
     */
    protected String removeFQDNRoot(String fQDNOrDomain) {
        return fQDNOrDomain.replaceAll("\\.$", "");
    }

}
