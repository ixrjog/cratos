package com.baiyi.cratos.facade;

import com.baiyi.cratos.domain.param.http.traffic.TrafficLayerTopologyParam;
import com.baiyi.cratos.domain.view.traffic.TrafficLayerTopologyVO;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/6/16 13:48
 * &#064;Version 1.0
 */
public interface TrafficLayerTopologyFacade {

    TrafficLayerTopologyVO.Topology queryTrafficLayerTopology(TrafficLayerTopologyParam.QueryTrafficLayerTopology queryTrafficLayerTopology);

}
