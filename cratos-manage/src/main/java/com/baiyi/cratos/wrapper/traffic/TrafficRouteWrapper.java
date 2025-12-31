package com.baiyi.cratos.wrapper.traffic;

import com.baiyi.cratos.annotation.BusinessWrapper;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.TrafficRoute;
import com.baiyi.cratos.domain.view.eds.EdsInstanceVO;
import com.baiyi.cratos.domain.view.traffic.TrafficRouteVO;
import com.baiyi.cratos.eds.dns.DNSResolver;
import com.baiyi.cratos.eds.dns.DNSResolverFactory;
import com.baiyi.cratos.domain.model.DNS;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.BaseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import static com.baiyi.cratos.annotation.BusinessWrapper.InvokeAts.BEFORE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/12/11 10:27
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TrafficRouteWrapper extends BaseDataTableConverter<TrafficRouteVO.Route, TrafficRoute> implements BaseWrapper<TrafficRouteVO.Route> {

    @Override
    @BusinessWrapper(invokeAt = BEFORE, types = {BusinessTypeEnum.BUSINESS_TAG, BusinessTypeEnum.BUSINESS_DOC, BusinessTypeEnum.ENV, BusinessTypeEnum.EDS_INSTANCE, BusinessTypeEnum.TRAFFIC_RECORD_TARGET})
    public void wrap(TrafficRouteVO.Route vo) {
        // 开始从DNS工厂中查询相关解析记录
        EdsInstanceVO.EdsInstance edsInstance = vo.getDnsResolverInstance();
        DNSResolver dnsResolver = DNSResolverFactory.getDNSResolver(edsInstance.getEdsType());
        if (dnsResolver == null) {
            return;
        }
        // Console URL
        try {
            vo.setConsoleUrl(dnsResolver.generateConsoleURL(vo.toTrafficRoute()));
        } catch (Exception e) {
             log.error(e.getMessage(), e);
        }
        DNS.ResourceRecordSet resourceRecordSet = dnsResolver.getDNSResourceRecordSet(vo.toTrafficRoute());
        // No data
        if (resourceRecordSet.isNoData()) {
            return;
        }
        wrapResourceRecordSet(vo, resourceRecordSet);
    }

    private void wrapResourceRecordSet(TrafficRouteVO.Route vo, DNS.ResourceRecordSet resourceRecordSet) {
        vo.setDnsResourceRecordSet(resourceRecordSet);
        if (CollectionUtils.isEmpty(vo.getRecordTargets())) {
            return;
        }
        vo.getRecordTargets()
                .forEach(recordTarget -> resourceRecordSet.getResourceRecords()
                        .forEach(dnsResourceRecord -> {
                            if (dnsResourceRecord.getValue()
                                    .equals(recordTarget.getRecordValue())) {
                                recordTarget.setDnsResourceRecord(dnsResourceRecord);
                            }
                        }));
    }

}