package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.domain.generator.TrafficLayerDomainRecord;
import com.baiyi.cratos.mapper.TrafficLayerDomainRecordMapper;
import com.baiyi.cratos.service.TrafficLayerDomainRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

/**
 * @Author baiyi
 * @Date 2024/3/29 10:57
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class TrafficLayerDomainRecordServiceImpl implements TrafficLayerDomainRecordService {

    private final TrafficLayerDomainRecordMapper trafficLayerDomainRecordMapper;

    @Override
    public TrafficLayerDomainRecord getByUniqueKey(TrafficLayerDomainRecord trafficLayerDomainRecord) {
        Example example = new Example(TrafficLayerDomainRecord.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("domainId", trafficLayerDomainRecord.getDomainId())
                .andEqualTo("envName", trafficLayerDomainRecord.getEnvName());
        return trafficLayerDomainRecordMapper.selectOneByExample(example);
    }

}
