package com.baiyi.cratos.wrapper.traffic;

import com.baiyi.cratos.annotation.BusinessDecorator;
import com.baiyi.cratos.common.util.IdentityUtils;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.TrafficRecordTarget;
import com.baiyi.cratos.domain.view.traffic.TrafficRouteVO;
import com.baiyi.cratos.service.TrafficRecordTargetService;
import com.baiyi.cratos.wrapper.base.BaseBusinessDecorator;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/12/11 13:35
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.TRAFFIC_RECORD_TARGET)
public class TrafficRouteTargetWrapper extends BaseDataTableConverter<TrafficRouteVO.RecordTarget, TrafficRecordTarget> implements BaseBusinessDecorator<TrafficRouteVO.HasRecordTargets, TrafficRouteVO.RecordTarget> {

    private final TrafficRecordTargetService trafficRecordTargetService;

    @Override
    @BusinessDecorator(types = {BusinessTypeEnum.BUSINESS_TAG, BusinessTypeEnum.BUSINESS_DOC})
    public void wrap(TrafficRouteVO.RecordTarget vo) {
    }

    @Override
    public void decorateBusiness(TrafficRouteVO.HasRecordTargets biz) {
        if (!IdentityUtils.hasIdentity(biz.getTrafficRouteId())) {
            return;
        }
        List<TrafficRecordTarget> targets = trafficRecordTargetService.queryByTrafficRouteId(biz.getTrafficRouteId());
        if (CollectionUtils.isEmpty(targets)) {
            return;
        }
        biz.setRecordTargets(targets.stream()
                                     .map(this::wrapToTarget)
                                     .toList());
    }

}
