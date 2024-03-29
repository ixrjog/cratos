package com.baiyi.cratos.facade;

import com.baiyi.cratos.domain.param.traffic.TrafficLayerDomainRecordParam;

/**
 * @Author baiyi
 * @Date 2024/3/29 17:40
 * @Version 1.0
 */
public interface TrafficLayerDomainRecordFacade {

    void addTrafficLayerDomainRecord(TrafficLayerDomainRecordParam.AddRecord addRecord);

    void updateTrafficLayerDomainRecord(TrafficLayerDomainRecordParam.UpdateRecord updateRecord);

}
