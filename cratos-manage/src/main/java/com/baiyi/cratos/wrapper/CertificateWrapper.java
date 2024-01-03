package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.domain.generator.Certificate;
import com.baiyi.cratos.domain.view.certificate.CertificateVO;
import com.baiyi.cratos.wrapper.base.IBaseWrapper;
import com.baiyi.cratos.wrapper.base.IDataTableConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/1/3 11:29
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class CertificateWrapper implements IDataTableConverter<CertificateVO.Certificate, Certificate>, IBaseWrapper<CertificateVO.Certificate> {

    @Override
    public void wrap(CertificateVO.Certificate certificate) {
        // tag.setBusinessTypeEnum(BusinessTypeEnum.getByType(tag.getBusinessType()));
        // tag.setQuantityUsed(businessTagService.countByTagId(tag.getId()));
    }

}