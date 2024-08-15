package com.baiyi.cratos.facade;

import com.baiyi.cratos.domain.param.traffic.TrafficLayerIngressParam;
import com.baiyi.cratos.domain.view.traffic.TrafficLayerIngressVO;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/8/14 上午10:49
 * &#064;Version 1.0
 */
public interface TrafficLayerIngressFacade {

    TrafficLayerIngressVO.IngressDetails queryIngressHostDetails(
            TrafficLayerIngressParam.QueryIngressHostDetails queryIngressHostDetails);

}
