package com.baiyi.cratos.wrapper.acme;

import com.baiyi.cratos.annotation.BusinessDecorator;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.AcmeCertificate;
import com.baiyi.cratos.domain.view.acme.AcmeCertificateVO;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.BaseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/3/30 11:00
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AcmeCertificateWrapper extends BaseDataTableConverter<AcmeCertificateVO.Certificate, AcmeCertificate> implements BaseWrapper<AcmeCertificateVO.Certificate> {

    @Override
    @BusinessDecorator(types = BusinessTypeEnum.ACME_CERTIFICATE_DEPLOYMENT)
    public void wrap(AcmeCertificateVO.Certificate vo) {
    }

}