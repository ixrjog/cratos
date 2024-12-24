package com.baiyi.cratos.wrapper.application;

import com.baiyi.cratos.domain.generator.ApplicationActuator;
import com.baiyi.cratos.domain.view.application.ApplicationActuatorVO;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.IBaseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/24 10:38
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationActuatorWrapper extends BaseDataTableConverter<ApplicationActuatorVO.ApplicationActuator, ApplicationActuator> implements IBaseWrapper<ApplicationActuatorVO.ApplicationActuator> {

    @Override
    public void wrap(ApplicationActuatorVO.ApplicationActuator vo) {
    }

}