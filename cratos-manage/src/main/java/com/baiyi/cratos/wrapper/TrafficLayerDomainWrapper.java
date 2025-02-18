package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.annotation.BusinessWrapper;
import com.baiyi.cratos.common.util.IdentityUtil;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.TrafficLayerDomain;
import com.baiyi.cratos.domain.view.traffic.TrafficLayerDomainVO;
import com.baiyi.cratos.service.TrafficLayerDomainRecordService;
import com.baiyi.cratos.service.TrafficLayerDomainService;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.IBusinessWrapper;
import com.baiyi.cratos.wrapper.builder.ResourceCountBuilder;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.baiyi.cratos.domain.enums.BusinessTypeEnum.TRAFFIC_LAYER_RECORD;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/3/29 13:38
 * &#064;Version  1.0
 */
@Component
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.TRAFFIC_LAYER_DOMAIN)
public class TrafficLayerDomainWrapper extends BaseDataTableConverter<TrafficLayerDomainVO.Domain, TrafficLayerDomain> implements IBusinessWrapper<TrafficLayerDomainVO.HasDomain, TrafficLayerDomainVO.Domain> {

    private final TrafficLayerDomainService domainService;
    private final TrafficLayerDomainRecordService recordService;

    @Override
    @BusinessWrapper(ofTypes = {BusinessTypeEnum.BUSINESS_TAG, BusinessTypeEnum.BUSINESS_DOC})
    public void wrap(TrafficLayerDomainVO.Domain vo) {
        Map<String, Integer> resourceCount = ResourceCountBuilder.newBuilder()
                .put(buildResourceCountForRecord(vo))
                .build();
        vo.setResourceCount(resourceCount);
    }

    @Override
    public void businessWrap(TrafficLayerDomainVO.HasDomain iDomain) {
        IdentityUtil.validIdentityRun(iDomain.getDomainId())
                .withTrue(() -> {
                    TrafficLayerDomain trafficLayerDomain = domainService.getById(iDomain.getDomainId());
                    TrafficLayerDomainVO.Domain domain = this.convert(trafficLayerDomain);
                    wrapFromProxy(domain);
                    iDomain.setDomain(domain);
                });
    }

    private Map<String, Integer> buildResourceCountForRecord(TrafficLayerDomainVO.Domain domain) {
        Map<String, Integer> resourceCount = Maps.newHashMap();
        resourceCount.put(TRAFFIC_LAYER_RECORD.name(), recordService.selectCountByDomainId(domain.getId()));
        return resourceCount;
    }

}