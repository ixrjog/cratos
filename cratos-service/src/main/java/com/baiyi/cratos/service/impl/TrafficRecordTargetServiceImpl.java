package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.annotation.DeleteBoundBusiness;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.TrafficRecordTarget;
import com.baiyi.cratos.mapper.TrafficRecordTargetMapper;
import com.baiyi.cratos.service.TrafficRecordTargetService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

import static com.baiyi.cratos.common.configuration.CachingConfiguration.RepositoryName.LONG_TERM;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/12/11 11:00
 * &#064;Version 1.0
 */
@Service
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.TRAFFIC_RECORD_TARGET)
public class TrafficRecordTargetServiceImpl implements TrafficRecordTargetService {

    private final TrafficRecordTargetMapper trafficRecordTargetMapper;

    @Override
    @CacheEvict(cacheNames = LONG_TERM, key = "'DOMAIN:TRAFFICRECORDTARGET:ID:' + #id")
    public void clearCacheById(int id) {
    }

    @Override
    public List<TrafficRecordTarget> queryByTrafficRouteId(Integer trafficRouteId) {
        Example example = new Example(TrafficRecordTarget.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("trafficRouteId", trafficRouteId);
        return trafficRecordTargetMapper.selectByExample(example);
    }

    @Override
    @DeleteBoundBusiness(businessId = "#id", targetTypes = {BusinessTypeEnum.BUSINESS_TAG, BusinessTypeEnum.BUSINESS_DOC})
    public void deleteById(int id) {
        TrafficRecordTargetService.super.deleteById(id);
    }

}