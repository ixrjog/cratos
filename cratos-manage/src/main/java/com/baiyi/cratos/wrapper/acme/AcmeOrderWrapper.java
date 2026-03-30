package com.baiyi.cratos.wrapper.acme;

import com.baiyi.cratos.annotation.BusinessDecorator;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.AcmeOrder;
import com.baiyi.cratos.domain.view.acme.AcmeOrderVO;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.BaseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/3/30 10:02
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AcmeOrderWrapper extends BaseDataTableConverter<AcmeOrderVO.Order, AcmeOrder> implements BaseWrapper<AcmeOrderVO.Order> {

    @Override
    @BusinessDecorator(types = {BusinessTypeEnum.BUSINESS_TAG, BusinessTypeEnum.BUSINESS_DOC, BusinessTypeEnum.ACME_ACCOUNT, BusinessTypeEnum.ACME_DOMAIN})
    public void wrap(AcmeOrderVO.Order vo) {
    }

}