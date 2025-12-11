package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.generator.TrafficRecordTarget;
import com.baiyi.cratos.mapper.TrafficRecordTargetMapper;
import com.baiyi.cratos.service.base.BaseValidService;
import com.baiyi.cratos.service.base.SupportBusinessService;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/12/11 11:00
 * &#064;Version 1.0
 */
public interface TrafficRecordTargetService extends BaseValidService<TrafficRecordTarget, TrafficRecordTargetMapper>, SupportBusinessService {

    List<TrafficRecordTarget> queryByTrafficRouteId(Integer trafficRouteId);

}

