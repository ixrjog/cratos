package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.common.exception.TrafficLayerException;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.TrafficLayerDomain;
import com.baiyi.cratos.domain.param.traffic.TrafficLayerDomainParam;
import com.baiyi.cratos.domain.view.traffic.TrafficLayerDomainVO;
import com.baiyi.cratos.facade.TrafficLayerDomainFacade;
import com.baiyi.cratos.service.TrafficLayerDomainRecordService;
import com.baiyi.cratos.service.TrafficLayerDomainService;
import com.baiyi.cratos.wrapper.TrafficLayerDomainWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/3/29 11:04
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TrafficLayerDomainFacadeImpl implements TrafficLayerDomainFacade {

    private final TrafficLayerDomainService domainService;

    private final TrafficLayerDomainRecordService recordService;

    private final TrafficLayerDomainWrapper domainWrapper;

    @Override
    public DataTable<TrafficLayerDomainVO.Domain> queryDomainPage(TrafficLayerDomainParam.DomainPageQuery pageQuery) {
        DataTable<TrafficLayerDomain> table = domainService.queryPageByParam(pageQuery);
        return domainWrapper.wrapToTarget(table);
    }

    @Override
    public void addTrafficLayerDomain(TrafficLayerDomainParam.AddDomain addDomain) {
        domainService.add(addDomain.toTarget());
    }

    @Override
    public void updateTrafficLayerDomain(TrafficLayerDomainParam.UpdateDomain updateDomain) {
        TrafficLayerDomain trafficLayerDomain = domainService.getById(updateDomain.getId());
        trafficLayerDomain.setName(updateDomain.getName());
        trafficLayerDomain.setComment(updateDomain.getComment());
        domainService.updateByPrimaryKey(trafficLayerDomain);
    }

    @Override
    public void deleteById(int id) {
        if (recordService.selectCountByDomainId(id) > 0) {
            throw new TrafficLayerException("Resolved record association.");
        }
        domainService.deleteById(id);
    }

}
