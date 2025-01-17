package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.annotation.DeleteBoundBusiness;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.TrafficLayerDomain;
import com.baiyi.cratos.domain.param.http.traffic.TrafficLayerDomainParam;
import com.baiyi.cratos.mapper.TrafficLayerDomainMapper;
import com.baiyi.cratos.service.TrafficLayerDomainService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
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
@BusinessType(type = BusinessTypeEnum.TRAFFIC_LAYER_DOMAIN)
public class TrafficLayerDomainServiceImpl implements TrafficLayerDomainService {

    private final TrafficLayerDomainMapper trafficLayerDomainMapper;

    @Override
    public TrafficLayerDomain getByUniqueKey(@NonNull TrafficLayerDomain record) {
        Example example = new Example(TrafficLayerDomain.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("domain", record.getDomain());
        return trafficLayerDomainMapper.selectOneByExample(example);
    }

    @Override
    public DataTable<TrafficLayerDomain> queryPageByParam(TrafficLayerDomainParam.DomainPageQueryParam pageQuery) {
        Page<?> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<TrafficLayerDomain> data = trafficLayerDomainMapper.queryPageByParam(pageQuery);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    @DeleteBoundBusiness(businessId = "#id", targetTypes = {BusinessTypeEnum.BUSINESS_TAG, BusinessTypeEnum.BUSINESS_DOC})
    public void deleteById(int id) {
        TrafficLayerDomainService.super.deleteById(id);
    }

    @Override
    @CacheEvict(cacheNames = LONG_TERM, key = "'DOMAIN:TRAFFICLAYERDOMAIN:ID:' + #id")
    public void clearCacheById(int id) {
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

}
