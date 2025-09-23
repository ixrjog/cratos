package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.annotation.BusinessWrapper;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.CratosInstance;
import com.baiyi.cratos.domain.view.cratos.CratosInstanceVO;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.BaseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/7 15:45
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CratosInstanceWrapper extends BaseDataTableConverter<CratosInstanceVO.RegisteredInstance, CratosInstance> implements BaseWrapper<CratosInstanceVO.RegisteredInstance> {

    @Override
    @BusinessWrapper(types = {BusinessTypeEnum.BUSINESS_TAG})
    public void wrap(CratosInstanceVO.RegisteredInstance vo) {
    }

}