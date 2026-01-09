package com.baiyi.cratos.eds.dns;


import com.baiyi.cratos.domain.generator.TrafficRoute;
import com.baiyi.cratos.domain.model.DNS;
import com.baiyi.cratos.domain.param.http.traffic.TrafficRouteParam;
import com.baiyi.cratos.eds.core.EdsInstanceTypeOfAnnotate;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.InitializingBean;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/12/11 16:06
 * &#064;Version 1.0
 */
public interface DNSResolver extends EdsInstanceTypeOfAnnotate, InitializingBean {

    DNS.ResourceRecordSet getDNSResourceRecordSet(TrafficRoute trafficRoute);

    void switchToRoute(TrafficRouteParam.SwitchRecordTarget switchRecordTarget);

    String getZoneId(TrafficRoute trafficRoute);

    String generateConsoleURL(TrafficRoute trafficRoute);

    default EdsAssetTypeEnum getAssetTypeEnum() {
        return AopUtils.getTargetClass(this)
                .getAnnotation(EdsInstanceAssetType.class)
                .assetTypeOf();
    }

    @Override
    default void afterPropertiesSet() {
        DNSResolverFactory.register(this);
    }

}
