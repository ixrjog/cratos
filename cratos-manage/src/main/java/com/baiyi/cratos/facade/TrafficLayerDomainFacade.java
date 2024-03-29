package com.baiyi.cratos.facade;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.traffic.TrafficLayerDomainParam;
import com.baiyi.cratos.domain.view.traffic.TrafficLayerDomainVO;

/**
 * @Author baiyi
 * @Date 2024/3/29 11:03
 * @Version 1.0
 */
public interface TrafficLayerDomainFacade {

    DataTable<TrafficLayerDomainVO.Domain> queryDomainPage(TrafficLayerDomainParam.DomainPageQuery pageQuery);

    void addTrafficLayerDomain(TrafficLayerDomainParam.AddDomain addDomain);

    void updateTrafficLayerDomain(TrafficLayerDomainParam.UpdateDomain updateDomain);

}
