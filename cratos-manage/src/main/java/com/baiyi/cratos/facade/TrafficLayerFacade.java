package com.baiyi.cratos.facade;

import com.baiyi.cratos.domain.param.traffic.TrafficLayerRecordParam;
import com.baiyi.cratos.domain.view.traffic.TrafficLayerRecordVO;

/**
 * @Author baiyi
 * @Date 2024/3/29 13:48
 * @Version 1.0
 */
public interface TrafficLayerFacade {

    TrafficLayerRecordVO.RecordDetails queryRecordDetails(TrafficLayerRecordParam.QueryRecordDetails queryRecordDetails);

    String queryRecordDetailsStringTable(TrafficLayerRecordParam.QueryRecordDetails queryRecordDetails);

}
