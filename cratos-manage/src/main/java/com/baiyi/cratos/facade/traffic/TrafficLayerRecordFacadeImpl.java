package com.baiyi.cratos.facade.traffic;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.TrafficLayerDomainRecord;
import com.baiyi.cratos.domain.param.http.traffic.TrafficLayerRecordParam;
import com.baiyi.cratos.domain.view.traffic.TrafficLayerRecordVO;
import com.baiyi.cratos.facade.TrafficLayerRecordFacade;
import com.baiyi.cratos.service.TrafficLayerDomainRecordService;
import com.baiyi.cratos.wrapper.traffic.TrafficLayerRecordWrapper;
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
public class TrafficLayerRecordFacadeImpl implements TrafficLayerRecordFacade {

    private final TrafficLayerDomainRecordService recordService;
    private final TrafficLayerRecordWrapper recordWrapper;

    @Override
    public void addTrafficLayerRecord(TrafficLayerRecordParam.AddRecord addRecord) {
        TrafficLayerDomainRecord trafficLayerDomainRecord = addRecord.toTarget();
        if (recordService.getByUniqueKey(trafficLayerDomainRecord) == null) {
            recordService.add(addRecord.toTarget());
        }
    }

    @Override
    public DataTable<TrafficLayerRecordVO.Record> queryRecordPage(TrafficLayerRecordParam.RecordPageQuery pageQuery) {
        DataTable<TrafficLayerDomainRecord> table = recordService.queryPageByParam(pageQuery);
        return recordWrapper.wrapToTarget(table);
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
