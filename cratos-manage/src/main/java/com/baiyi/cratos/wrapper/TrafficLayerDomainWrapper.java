package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.annotation.BusinessWrapper;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.TrafficLayerDomain;
import com.baiyi.cratos.domain.view.traffic.TrafficLayerDomainVO;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.IBaseWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/3/29 13:38
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class TrafficLayerDomainWrapper extends BaseDataTableConverter<TrafficLayerDomainVO.Domain, TrafficLayerDomain> implements IBaseWrapper<TrafficLayerDomainVO.Domain> {

    @Override
    @BusinessWrapper(ofTypes = {BusinessTypeEnum.BUSINESS_TAG, BusinessTypeEnum.BUSINESS_DOC})
    public void wrap(TrafficLayerDomainVO.Domain domain) {
    }

}