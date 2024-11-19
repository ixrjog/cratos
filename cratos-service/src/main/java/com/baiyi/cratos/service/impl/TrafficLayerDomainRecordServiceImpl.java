package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.annotation.DeleteBoundBusiness;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.TrafficLayerDomainRecord;
import com.baiyi.cratos.domain.param.traffic.TrafficLayerRecordParam;
import com.baiyi.cratos.mapper.TrafficLayerDomainRecordMapper;
import com.baiyi.cratos.service.TrafficLayerDomainRecordService;
import com.baiyi.cratos.util.SqlHelper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

import static com.baiyi.cratos.common.configuration.CachingConfiguration.RepositoryName.LONG_TERM;

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
    public DataTable<TrafficLayerDomainRecord> queryPageByParam(TrafficLayerRecordParam.RecordPageQuery pageQuery) {
        Page<?> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        Example example = new Example(TrafficLayerDomainRecord.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(pageQuery.getQueryName())) {
            criteria.andLike("recordName", SqlHelper.toLike(pageQuery.getQueryName()));
        }
        if (pageQuery.getDomainId() != null) {
            criteria.andEqualTo("domainId", pageQuery.getDomainId());
        }
        List<TrafficLayerDomainRecord> data = trafficLayerDomainRecordMapper.selectByExample(example);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public TrafficLayerDomainRecord getByUniqueKey(@NonNull TrafficLayerDomainRecord record) {
        Example example = new Example(TrafficLayerDomainRecord.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("domainId", record.getDomainId())
                .andEqualTo("envName", record.getEnvName());
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
        TrafficLayerDomainRecordService.super.deleteById(id);
    }

    @Override
    @CacheEvict(cacheNames = LONG_TERM, key = "'DOMAIN:TRAFFICLAYERDOMAINRECORD:ID:' + #id")
    public void clearCacheById(int id) {
    }

}
