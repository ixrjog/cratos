package com.baiyi.cratos.eds.dns;

import com.baiyi.cratos.common.exception.TrafficRouteException;
import com.baiyi.cratos.domain.generator.TrafficRecordTarget;
import com.baiyi.cratos.domain.generator.TrafficRoute;
import com.baiyi.cratos.eds.core.config.base.HasEdsConfig;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.TrafficRecordTargetService;
import com.baiyi.cratos.service.TrafficRouteService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;

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

    private static final int MAX_LOAD_BALANCING = 2;

    protected TrafficRecordTarget getTrafficRecordTargetById(int trafficRecordTargetId) {
        TrafficRecordTarget trafficRecordTarget = trafficRecordTargetService.getById(trafficRecordTargetId);
        if (trafficRecordTarget == null) {
            TrafficRouteException.runtime("TrafficRecordTarget 不存在, recordTargetId: {}", trafficRecordTargetId);
        }
        return trafficRecordTarget;
    }

    protected TrafficRoute getTrafficRouteById(int trafficRouteId) {
        TrafficRoute trafficRoute = trafficRouteService.getById(trafficRouteId);
        if (trafficRoute == null) {
            TrafficRouteException.runtime("TrafficRoute 不存在, trafficRouteId: {}", trafficRouteId);
        }
        return trafficRoute;
    }

    @SuppressWarnings("unchecked")
    protected Config getEdsConfig(TrafficRoute trafficRoute, EdsAssetTypeEnum assetTypeEnum) {
        EdsInstanceProviderHolder<Config, ?> holder = (EdsInstanceProviderHolder<Config, ?>) edsInstanceProviderHolderBuilder.newHolder(
                trafficRoute.getDnsResolverInstanceId(), assetTypeEnum.name());
        return holder.getInstance()
                .getConfig();
    }

    protected String getRR(TrafficRoute trafficRoute) {
        return StringUtils.removeEnd(trafficRoute.getDomainRecord(), "." + trafficRoute.getDomain());
    }

    protected String toFQDN(String domainName) {
        if (domainName == null || domainName.isEmpty()) {
            return domainName;
        }
        return domainName.endsWith(".") ? domainName : domainName + ".";
    }

    protected abstract List<Record> getTrafficRouteRecords(Config config, TrafficRoute trafficRoute);

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
