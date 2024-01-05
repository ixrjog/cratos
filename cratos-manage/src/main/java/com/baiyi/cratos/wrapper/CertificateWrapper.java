package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.annotation.BusinessWrapper;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.Certificate;
import com.baiyi.cratos.domain.view.certificate.CertificateVO;
import com.baiyi.cratos.wrapper.base.IBaseWrapper;
import com.baiyi.cratos.wrapper.base.IDataTableConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/1/3 11:29
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = {@Lazy})
public class CertificateWrapper implements IDataTableConverter<CertificateVO.Certificate, Certificate>, IBaseWrapper<CertificateVO.Certificate> {

    private final CertificateWrapper certificateWrapper;

    @Override
    @BusinessWrapper(businessEnums = {BusinessTypeEnum.BUSINESS_TAG})
    public void wrap(CertificateVO.Certificate certificate) {
        // TODO
    }

    @Override
    public Class<CertificateVO.Certificate> getTargetClazz() {
        return CertificateVO.Certificate.class;
    }

    @Override
    public IBaseWrapper<CertificateVO.Certificate> getBean() {
        return certificateWrapper;
    }

}