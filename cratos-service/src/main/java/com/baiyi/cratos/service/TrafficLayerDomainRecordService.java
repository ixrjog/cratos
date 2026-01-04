package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.TrafficLayerDomainRecord;
import com.baiyi.cratos.domain.param.http.traffic.TrafficLayerRecordParam;
import com.baiyi.cratos.mapper.TrafficLayerDomainRecordMapper;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;
import com.baiyi.cratos.service.base.BaseValidService;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/3/29 10:40
 * @Version 1.0
 */
public interface TrafficLayerDomainRecordService extends BaseUniqueKeyService<TrafficLayerDomainRecord, TrafficLayerDomainRecordMapper>, BaseValidService<TrafficLayerDomainRecord, TrafficLayerDomainRecordMapper> {

    DataTable<TrafficLayerDomainRecord> queryPageByParam(TrafficLayerRecordParam.RecordPageQuery pageQuery);

    List<TrafficLayerDomainRecord> queryByRecordName(String recordName);

    int selectCountByDomainId(int domainId);

}
