package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.domain.generator.TrafficLayerDomainRecord;
import com.baiyi.cratos.domain.param.traffic.TrafficLayerRecordParam;
import com.baiyi.cratos.domain.view.traffic.TrafficLayerRecordVO;
import com.baiyi.cratos.facade.TrafficLayerFacade;
import com.baiyi.cratos.facade.proxy.TrafficLayerProxy;
import com.baiyi.cratos.service.TrafficLayerDomainRecordService;
import com.baiyi.cratos.wrapper.TrafficLayerRecordWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/3/29 13:48
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TrafficLayerFacadeImpl implements TrafficLayerFacade {

    private final TrafficLayerDomainRecordService recordService;

    private final TrafficLayerRecordWrapper recordWrapper;

    private final TrafficLayerProxy trafficLayerProxy;

    @Override
    public TrafficLayerRecordVO.RecordDetails queryRecordDetails(TrafficLayerRecordParam.QueryRecordDetails queryRecordDetails) {
        TrafficLayerDomainRecord uniqueKey = queryRecordDetails.toTrafficLayerRecord();
        TrafficLayerDomainRecord trafficLayerDomainRecord = recordService.getByUniqueKey(uniqueKey);
        return trafficLayerDomainRecord == null ? TrafficLayerRecordVO.RecordDetails.NOT_FOUND :
                TrafficLayerRecordVO.RecordDetails.builder()
                        .recordId(trafficLayerDomainRecord.getId())
                        .record(recordWrapper.wrapToTarget(trafficLayerDomainRecord))
                        .originServer(trafficLayerProxy.buildOriginServer(trafficLayerDomainRecord.getRecordName(), trafficLayerDomainRecord.getOriginServer()))
                        .build();
    }

}
