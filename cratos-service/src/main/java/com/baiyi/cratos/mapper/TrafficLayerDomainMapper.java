package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.TrafficLayerDomain;
import com.baiyi.cratos.domain.param.http.traffic.TrafficLayerDomainParam;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface TrafficLayerDomainMapper extends Mapper<TrafficLayerDomain> {

    List<TrafficLayerDomain> queryPageByParam(TrafficLayerDomainParam.DomainPageQueryParam pageQuery);

}