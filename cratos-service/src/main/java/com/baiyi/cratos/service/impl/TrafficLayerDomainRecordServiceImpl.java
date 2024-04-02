package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.annotation.DeleteBoundBusiness;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
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
@BusinessType(type = BusinessTypeEnum.TRAFFIC_LAYER_RECORD)
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

    @Override
    public int selectCountByDomainId(int domainId) {
        Example example = new Example(TrafficLayerDomainRecord.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("domainId", domainId);
        return trafficLayerDomainRecordMapper.selectCountByExample(example);
    }

    @Override
    @DeleteBoundBusiness(businessId = "#id", targetTypes = {BusinessTypeEnum.BUSINESS_TAG, BusinessTypeEnum.BUSINESS_DOC})
    public void deleteById(int id) {
        trafficLayerDomainRecordMapper.deleteByPrimaryKey(id);
    }

}
