package com.baiyi.cratos.facade;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.traffic.TrafficLayerRecordParam;
import com.baiyi.cratos.domain.view.traffic.TrafficLayerRecordVO;

/**
 * @Author baiyi
 * @Date 2024/3/29 17:40
 * @Version 1.0
 */
public interface TrafficLayerRecordFacade {

    void addTrafficLayerRecord(TrafficLayerRecordParam.AddRecord addRecord);

    void updateTrafficLayerRecord(TrafficLayerRecordParam.UpdateRecord updateRecord);

    void deleteById(int id);

    DataTable<TrafficLayerRecordVO.Record> queryRecordPage(TrafficLayerRecordParam.RecordPageQuery pageQuery);

}
