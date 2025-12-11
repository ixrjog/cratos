package com.baiyi.cratos.wrapper.traffic;

import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.TrafficRecordTarget;
import com.baiyi.cratos.domain.view.traffic.TrafficRouteVO;
import com.baiyi.cratos.wrapper.base.BaseBusinessWrapper;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/12/11 13:35
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.TRAFFIC_RECORD_TARGET)
public class TrafficRouteTargetWrapper extends BaseDataTableConverter<TrafficRouteVO.RecordTarget, TrafficRecordTarget> implements BaseBusinessWrapper<TrafficRouteVO.HasRecordTargets,TrafficRouteVO.RecordTarget> {

    @Override
    public void decorateBusiness(TrafficRouteVO.HasRecordTargets biz) {

    }

    @Override
    public void wrap(TrafficRouteVO.RecordTarget vo) {

    }

}
