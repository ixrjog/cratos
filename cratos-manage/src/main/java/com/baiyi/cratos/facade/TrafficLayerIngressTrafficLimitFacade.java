package com.baiyi.cratos.facade;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.http.traffic.TrafficIngressTrafficLimitParam;
import com.baiyi.cratos.domain.view.traffic.TrafficLayerIngressVO;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/9 13:42
 * &#064;Version 1.0
 */
public interface TrafficLayerIngressTrafficLimitFacade {

    DataTable<TrafficLayerIngressVO.IngressTrafficLimit> queryIngressTrafficLimitPage(
            TrafficIngressTrafficLimitParam.IngressTrafficLimitPageQuery pageQuery);

}
