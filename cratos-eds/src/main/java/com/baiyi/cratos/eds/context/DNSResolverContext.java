package com.baiyi.cratos.eds.context;

import com.baiyi.cratos.eds.core.holder.EdsProviderHolderFactory;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.TrafficRecordTargetService;
import com.baiyi.cratos.service.TrafficRouteService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/3/20 15:24
 * &#064;Version 1.0
 */
@Getter
@Component
@RequiredArgsConstructor
public class DNSResolverContext {

    protected final EdsAssetService edsAssetService;
    protected final TrafficRouteService trafficRouteService;
    protected final TrafficRecordTargetService trafficRecordTargetService;
    protected final EdsProviderHolderFactory edsProviderHolderFactory;

}
