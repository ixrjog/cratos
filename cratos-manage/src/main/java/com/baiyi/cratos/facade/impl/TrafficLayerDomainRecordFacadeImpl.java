package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.domain.generator.TrafficLayerDomainRecord;
import com.baiyi.cratos.domain.param.traffic.TrafficLayerRecordParam;
import com.baiyi.cratos.facade.TrafficLayerRecordFacade;
import com.baiyi.cratos.service.TrafficLayerDomainRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/3/29 17:41
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TrafficLayerDomainRecordFacadeImpl implements TrafficLayerRecordFacade {

    private final TrafficLayerDomainRecordService recordService;

    @Override
    public void addTrafficLayerRecord(TrafficLayerRecordParam.AddRecord addRecord) {
        recordService.add(addRecord.toTarget());
    }

    @Override
    public void updateTrafficLayerRecord(TrafficLayerRecordParam.UpdateRecord updateRecord) {
        TrafficLayerDomainRecord trafficLayerDomainRecord = recordService.getById(updateRecord.getId());
        trafficLayerDomainRecord.setEnvName(updateRecord.getEnvName());
        trafficLayerDomainRecord.setRecordName(updateRecord.getRecordName());
        trafficLayerDomainRecord.setRouteTrafficTo(updateRecord.getRouteTrafficTo());
        trafficLayerDomainRecord.setOriginServer(updateRecord.getOriginServer());
        recordService.updateByPrimaryKey(trafficLayerDomainRecord);
    }

    @Override
    public void deleteById(int id) {
        recordService.deleteById(id);
    }

}
