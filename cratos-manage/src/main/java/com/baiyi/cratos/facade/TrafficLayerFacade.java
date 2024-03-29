package com.baiyi.cratos.facade;

import com.baiyi.cratos.domain.param.traffic.TrafficLayerDomainRecordParam;
import com.baiyi.cratos.domain.view.traffic.TrafficLayerDomainRecordVO;

/**
 * @Author baiyi
 * @Date 2024/3/29 13:48
 * @Version 1.0
 */
public interface TrafficLayerFacade {

    TrafficLayerDomainRecordVO.RecordDetails queryRecordDetails(TrafficLayerDomainRecordParam.QueryRecordDetails queryRecordDetails);

}
