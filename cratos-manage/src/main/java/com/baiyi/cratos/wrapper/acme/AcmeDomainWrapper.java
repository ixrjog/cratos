package com.baiyi.cratos.wrapper.acme;

import com.baiyi.cratos.annotation.BusinessDecorator;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.AcmeDomain;
import com.baiyi.cratos.domain.view.acme.AcmeDomainVO;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.BaseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/3/27 15:49
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AcmeDomainWrapper extends BaseDataTableConverter<AcmeDomainVO.Domain, AcmeDomain> implements BaseWrapper<AcmeDomainVO.Domain> {

    @Override
    @BusinessDecorator(types = {BusinessTypeEnum.BUSINESS_TAG, BusinessTypeEnum.BUSINESS_DOC, BusinessTypeEnum.ACME_ACCOUNT, BusinessTypeEnum.EDS_INSTANCE})
    public void wrap(AcmeDomainVO.Domain vo) {
    }

}