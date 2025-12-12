package com.baiyi.cratos.eds.dns;

import com.baiyi.cratos.domain.generator.TrafficRoute;
import com.baiyi.cratos.eds.core.EdsInstanceTypeOfAnnotate;
import com.baiyi.cratos.eds.dns.model.DNS;
import org.springframework.beans.factory.InitializingBean;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/12/11 16:06
 * &#064;Version 1.0
 */
public interface DNSResolver extends EdsInstanceTypeOfAnnotate, InitializingBean {

    DNS.ResourceRecordSet getDNSResourceRecordSet(TrafficRoute trafficRoute);

    @Override
    default void afterPropertiesSet() {
        DNSResolverFactory.register(this);
    }

}
