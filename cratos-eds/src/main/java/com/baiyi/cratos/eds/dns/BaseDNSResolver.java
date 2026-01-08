package com.baiyi.cratos.eds.dns;

import com.baiyi.cratos.common.exception.TrafficRouteException;
import com.baiyi.cratos.domain.generator.TrafficRecordTarget;
import com.baiyi.cratos.domain.generator.TrafficRoute;
import com.baiyi.cratos.domain.model.DNS;
import com.baiyi.cratos.domain.param.http.traffic.TrafficRouteParam;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.config.base.HasEdsConfig;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.eds.dnsgoogle.enums.DnsRRType;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.TrafficRecordTargetService;
import com.baiyi.cratos.service.TrafficRouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.support.AopUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/12/11 16:11
 * &#064;Version 1.0
 */
@RequiredArgsConstructor
public abstract class BaseDNSResolver<Config extends HasEdsConfig, Record> implements DNSResolver {

    protected final EdsAssetService edsAssetService;
    protected final TrafficRouteService trafficRouteService;
    protected final TrafficRecordTargetService trafficRecordTargetService;
    protected final EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder;

    private static final int MAX_LOAD_BALANCING = 3;

    protected TrafficRecordTarget getTrafficRecordTargetById(int trafficRecordTargetId) {
        return Optional.ofNullable(trafficRecordTargetService.getById(trafficRecordTargetId))
                .orElseThrow(() -> new TrafficRouteException(
                        "TrafficRecordTarget 不存在, recordTargetId: {}",
                        trafficRecordTargetId
                ));
    }

    protected TrafficRoute getTrafficRouteById(int trafficRouteId) {
        return Optional.ofNullable(trafficRouteService.getById(trafficRouteId))
                .orElseThrow(
                        () -> new TrafficRouteException("TrafficRoute 不存在, trafficRouteId: {}", trafficRouteId));
    }

    protected EdsAssetTypeEnum getAssetTypeEnum() {
        return AopUtils.getTargetClass(this)
                .getAnnotation(EdsInstanceAssetType.class)
                .assetTypeOf();
    }

    protected SwitchRecordTargetContext<Config, Record> buildSwitchContext(
            TrafficRouteParam.SwitchRecordTarget switchRecordTarget) {
        TrafficRecordTarget trafficRecordTarget = getTrafficRecordTargetById(switchRecordTarget.getRecordTargetId());
        TrafficRoute trafficRoute = getTrafficRouteById(trafficRecordTarget.getTrafficRouteId());
        Config config = getEdsConfig(trafficRoute, getAssetTypeEnum());
        List<Record> matchedRecords = getTrafficRouteRecords(config, trafficRoute);
        validateRecordCount(matchedRecords);
        DnsRRType dnsRRType = DnsRRType.valueOf(trafficRecordTarget.getRecordType());
        return SwitchRecordTargetContext.<Config, Record>builder()
                .dnsRRType(dnsRRType)
                .conflictingDnsRRType(getConflictingDnsRRType(dnsRRType))
                .switchRecordTarget(switchRecordTarget)
                .trafficRecordTarget(trafficRecordTarget)
                .trafficRoute(trafficRoute)
                .matchedRecords(matchedRecords)
                .matchedRecordMap(toMatchedRecordMap(matchedRecords))
                .config(config)
                .build();
    }

    abstract protected Map<String, List<Record>> toMatchedRecordMap(List<Record> records);

    abstract protected DNS.ResourceRecordSet findMatchedRecord(List<Record> records, TrafficRoute trafficRoute);

    private DnsRRType getConflictingDnsRRType(DnsRRType dnsRRType) {
        return dnsRRType.equals(DnsRRType.CNAME) ? DnsRRType.A : DnsRRType.CNAME;
    }

    @SuppressWarnings("unchecked")
    protected Config getEdsConfig(TrafficRoute trafficRoute, EdsAssetTypeEnum assetTypeEnum) {
        EdsInstanceProviderHolder<Config, ?> holder = (EdsInstanceProviderHolder<Config, ?>) edsInstanceProviderHolderBuilder.newHolder(
                trafficRoute.getDnsResolverInstanceId(), assetTypeEnum.name());
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

    protected abstract List<Record> getTrafficRouteRecords(Config config, TrafficRoute trafficRoute);

    protected abstract void handleSingleTargetRouting(SwitchRecordTargetContext<Config, Record> context);

    protected void validateRecordCount(List<Record> matchedRecords) {
        if (!CollectionUtils.isEmpty(matchedRecords) && matchedRecords.size() > MAX_LOAD_BALANCING) {
            TrafficRouteException.runtime(
                    "Current routing load balancing count exceeds maximum: max routing count {}, current routing count {}",
                    MAX_LOAD_BALANCING, matchedRecords.size()
            );
        }
    }

    @Override
    public String getZoneId(TrafficRoute trafficRoute) {
        return trafficRoute.getDomainRecord();
    }

}
