package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.domain.generator.TrafficLayerDomainRecord;
import com.baiyi.cratos.domain.view.traffic.TrafficLayerDomainRecordVO;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.IBaseWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/3/29 14:49
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class TrafficLayerDomainRecordWrapper extends BaseDataTableConverter<TrafficLayerDomainRecordVO.Record, TrafficLayerDomainRecord> implements IBaseWrapper<TrafficLayerDomainRecordVO.Record> {

    @Override
    public void wrap(TrafficLayerDomainRecordVO.Record record) {
    }

}