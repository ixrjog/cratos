package com.baiyi.cratos.service.impl;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.TrafficRoute;
import com.baiyi.cratos.domain.param.http.traffic.TrafficRouteParam;
import com.baiyi.cratos.mapper.TrafficRouteMapper;
import com.baiyi.cratos.service.TrafficRouteService;
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
 * &#064;Author  baiyi
 * &#064;Date  2025/12/11 09:51
 * &#064;Version 1.0
 */
@Service
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.TRAFFIC_ROUTE)
public class TrafficRouteServiceImpl implements TrafficRouteService {

    private final TrafficRouteMapper trafficRouteMapper;

    @Override
    public DataTable<TrafficRoute> queryPageByParam(TrafficRouteParam.RoutePageQueryParam pageQuery) {
        Page<?> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<TrafficRoute> data = trafficRouteMapper.queryPageByParam(pageQuery);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    @CacheEvict(cacheNames = LONG_TERM, key = "'DOMAIN:TRAFFICROUTE:ID:' + #id")
    public void clearCacheById(int id) {
    }

    @Override
    public TrafficRoute getByUniqueKey(@NonNull TrafficRoute record) {
        Example example = new Example(TrafficRoute.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("domainRecord", record.getDomainRecord());
        return trafficRouteMapper.selectOneByExample(example);
    }

    @Override
    public TrafficRoute getByDomainRecord(@NonNull String domainRecord) {
        return getByUniqueKey(TrafficRoute.builder()
                                      .domainRecord(domainRecord)
                                      .build());
    }


}
