package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.annotation.BusinessWrapper;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.RiskEvent;
import com.baiyi.cratos.domain.view.risk.RiskEventVO;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.IBaseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/4/15 下午3:35
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.RISK_EVENT)
public class RiskEventWrapper extends BaseDataTableConverter<RiskEventVO.Event, RiskEvent> implements IBaseWrapper<RiskEventVO.Event> {

    @Override
    @BusinessWrapper(types = {BusinessTypeEnum.BUSINESS_TAG, BusinessTypeEnum.BUSINESS_DOC, BusinessTypeEnum.RISK_EVENT_IMPACT})
    public void wrap(RiskEventVO.Event vo) {
    }

}