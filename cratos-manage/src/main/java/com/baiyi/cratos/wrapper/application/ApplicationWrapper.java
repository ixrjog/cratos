package com.baiyi.cratos.wrapper.application;

import com.baiyi.cratos.annotation.BusinessWrapper;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.Application;
import com.baiyi.cratos.domain.view.application.ApplicationVO;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.IBaseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/15 11:28
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationWrapper extends BaseDataTableConverter<ApplicationVO.Application, Application> implements IBaseWrapper<ApplicationVO.Application> {

    private final ApplicationResourceWrapper resourceWrapper;

    @Override
    @BusinessWrapper(ofTypes = {BusinessTypeEnum.BUSINESS_TAG, BusinessTypeEnum.BUSINESS_DOC})
    public void wrap(ApplicationVO.Application vo) {
        resourceWrapper.wrap(vo);
    }

}