package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.annotation.BusinessWrapper;
import com.baiyi.cratos.common.util.IdentityUtil;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.TrafficLayerDomain;
import com.baiyi.cratos.domain.view.traffic.TrafficLayerDomainVO;
import com.baiyi.cratos.service.TrafficLayerDomainService;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.IBusinessWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/3/29 13:38
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.TRAFFIC_LAYER_DOMAIN)
public class TrafficLayerDomainWrapper extends BaseDataTableConverter<TrafficLayerDomainVO.Domain, TrafficLayerDomain> implements IBusinessWrapper<TrafficLayerDomainVO.IDomain, TrafficLayerDomainVO.Domain> {

    private final TrafficLayerDomainService domainService;

    @Override
    @BusinessWrapper(ofTypes = {BusinessTypeEnum.BUSINESS_TAG, BusinessTypeEnum.BUSINESS_DOC})
    public void wrap(TrafficLayerDomainVO.Domain domain) {
    }

    @Override
    public void businessWrap(TrafficLayerDomainVO.IDomain iDomain) {
        IdentityUtil.validIdentityRun(iDomain.getDomainId())
                .withTrue(() -> {
                    TrafficLayerDomain trafficLayerDomain = domainService.getById(iDomain.getDomainId());
                    TrafficLayerDomainVO.Domain domain = this.convert(trafficLayerDomain);
                    wrapFromProxy(domain);
                    iDomain.setDomain(domain);
                });
    }

}