package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.annotation.DeleteBoundBusiness;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.TrafficLayerDomain;
import com.baiyi.cratos.domain.param.traffic.TrafficLayerDomainParam;
import com.baiyi.cratos.mapper.TrafficLayerDomainMapper;
import com.baiyi.cratos.service.TrafficLayerDomainService;
import com.baiyi.cratos.util.SqlHelper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

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
    public TrafficLayerDomain getByUniqueKey(TrafficLayerDomain trafficLayerDomain) {
        Example example = new Example(TrafficLayerDomain.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("domain", trafficLayerDomain.getDomain());
        return trafficLayerDomainMapper.selectOneByExample(example);
    }

    @Override
    public DataTable<TrafficLayerDomain> queryPageByParam(TrafficLayerDomainParam.DomainPageQuery pageQuery) {
        Page<?> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        Example example = new Example(TrafficLayerDomain.class);
        if (StringUtils.isNotBlank(pageQuery.getQueryName())) {
            Example.Criteria criteria = example.createCriteria();
            criteria.andLike("domain", SqlHelper.toLike(pageQuery.getQueryName()));
        }
        //example.setOrderByClause("create_time");
        List<TrafficLayerDomain> data = trafficLayerDomainMapper.selectByExample(example);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    @DeleteBoundBusiness(businessId = "#id", targetTypes = {BusinessTypeEnum.BUSINESS_TAG, BusinessTypeEnum.BUSINESS_DOC})
    public void deleteById(int id) {
        trafficLayerDomainMapper.deleteByPrimaryKey(id);
    }

}
