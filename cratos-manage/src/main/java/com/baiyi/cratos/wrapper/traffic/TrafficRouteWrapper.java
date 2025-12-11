package com.baiyi.cratos.wrapper.traffic;

import com.baiyi.cratos.annotation.BusinessWrapper;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.TrafficRoute;
import com.baiyi.cratos.domain.view.traffic.TrafficRouteVO;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.BaseWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/12/11 10:27
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
public class TrafficRouteWrapper extends BaseDataTableConverter<TrafficRouteVO.Route, TrafficRoute> implements BaseWrapper<TrafficRouteVO.Route> {

    @Override
    @BusinessWrapper(types = {BusinessTypeEnum.BUSINESS_TAG, BusinessTypeEnum.BUSINESS_DOC, BusinessTypeEnum.ENV, BusinessTypeEnum.TRAFFIC_RECORD_TARGET})
    public void wrap(TrafficRouteVO.Route vo) {
    }

}