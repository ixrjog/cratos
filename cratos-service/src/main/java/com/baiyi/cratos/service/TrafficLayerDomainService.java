package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.TrafficLayerDomain;
import com.baiyi.cratos.domain.param.traffic.TrafficLayerDomainParam;
import com.baiyi.cratos.mapper.TrafficLayerDomainMapper;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;
import com.baiyi.cratos.service.base.BaseValidService;
import com.baiyi.cratos.service.base.SupportBusinessService;

/**
 * @Author baiyi
 * @Date 2024/3/29 10:39
 * @Version 1.0
 */
public interface TrafficLayerDomainService extends BaseUniqueKeyService<TrafficLayerDomain, TrafficLayerDomainMapper>, BaseValidService<TrafficLayerDomain, TrafficLayerDomainMapper>, SupportBusinessService {

    DataTable<TrafficLayerDomain> queryPageByParam(TrafficLayerDomainParam.DomainPageQueryParam pageQuery);

}
