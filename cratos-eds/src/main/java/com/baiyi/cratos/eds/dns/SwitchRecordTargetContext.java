package com.baiyi.cratos.eds.dns;

import com.baiyi.cratos.domain.generator.TrafficRecordTarget;
import com.baiyi.cratos.domain.generator.TrafficRoute;
import com.baiyi.cratos.domain.param.http.traffic.TrafficRouteParam;
import com.baiyi.cratos.eds.core.config.base.HasEdsConfig;
import com.baiyi.cratos.eds.dnsgoogle.enums.DnsRRType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static com.baiyi.cratos.eds.cloudflare.repo.CloudflareDnsRepo.PROXIED;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/12/19 10:10
 * &#064;Version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SwitchRecordTargetContext<Config extends HasEdsConfig, Record> {

    private TrafficRouteParam.SwitchRecordTarget switchRecordTarget;
    private Config config;
    private TrafficRoute trafficRoute;
    private TrafficRecordTarget trafficRecordTarget;
    private List<Record> matchedRecords;

    public List<Record> getMatchedRecords() {
        if (matchedRecords == null) {
            return List.of();
        }
        return matchedRecords;
    }

    public boolean isMatchedRecordsEmpty() {
        return CollectionUtils.isEmpty(matchedRecords);
    }

    public DnsRRType getDnsRRType() {
        return DnsRRType.valueOf(trafficRecordTarget.getRecordType());
    }

    public String getDomain() {
        return trafficRoute.getDomain();
    }

    public String getRR() {
        return StringUtils.removeEnd(trafficRoute.getDomainRecord(), "." + trafficRoute.getDomain());
    }

    public String getRecordValue() {
        return trafficRecordTarget.getRecordValue();
    }

    public Long getTTL() {
        return trafficRecordTarget.getTtl();
    }

    public Long getTTL(long defaultTTL) {
        return trafficRecordTarget.getTtl() == null ? defaultTTL : trafficRecordTarget.getTtl();
    }

    public String getResourceRecord() {
        return trafficRecordTarget.getResourceRecord();
    }

    public boolean getProxied() {
        return switchRecordTarget.getProxied() != null ? switchRecordTarget.getProxied() : PROXIED;
    }

    public int getMatchedRecordsSize() {
        if (CollectionUtils.isEmpty(matchedRecords)) {
            return 0;
        }
        return matchedRecords.size();
    }

}
