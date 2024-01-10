package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.annotation.BusinessWrapper;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.Certificate;
import com.baiyi.cratos.domain.view.certificate.CertificateVO;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.IBaseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/1/3 11:29
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CertificateWrapper extends BaseDataTableConverter<CertificateVO.Certificate, Certificate> implements IBaseWrapper<CertificateVO.Certificate> {

    @Override
    @BusinessWrapper(types = {BusinessTypeEnum.BUSINESS_TAG, BusinessTypeEnum.BUSINESS_DOC})
    public void wrap(CertificateVO.Certificate certificate) {
        // This is a good idea
    }

}