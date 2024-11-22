package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.TrafficLayerDomain;
import com.baiyi.cratos.domain.param.http.traffic.TrafficLayerDomainParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TrafficLayerDomainMapper extends Mapper<TrafficLayerDomain> {

    List<TrafficLayerDomain> queryPageByParam(TrafficLayerDomainParam.DomainPageQueryParam pageQuery);

}