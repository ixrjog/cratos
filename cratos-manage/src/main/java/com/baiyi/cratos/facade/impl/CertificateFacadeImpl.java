package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.common.util.BeanCopierUtil;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.Certificate;
import com.baiyi.cratos.domain.param.certificate.CertificateParam;
import com.baiyi.cratos.domain.view.certificate.CertificateVO;
import com.baiyi.cratos.facade.CertificateFacade;
import com.baiyi.cratos.service.CertificateService;
import com.baiyi.cratos.wrapper.CertificateWrapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/1/3 11:27
 * @Version 1.0
 */
@Component
@AllArgsConstructor
public class CertificateFacadeImpl implements CertificateFacade {

    private final CertificateService certificateService;

    private final CertificateWrapper certificateWrapper;

    @Override
    public void addCertificate(CertificateParam.AddCertificate addCertificate) {
        certificateService.add(toDO(addCertificate));
    }

    private Certificate toDO(Object param) {
        return BeanCopierUtil.copyProperties(param, Certificate.class);
    }

    @Override
    public void updateCertificate(CertificateParam.UpdateCertificate updateCertificate) {
        certificateService.updateByPrimaryKey(toDO(updateCertificate));
    }

    @Override
    public DataTable<CertificateVO.Certificate> queryCertificatePage(CertificateParam.CertificatePageQuery pageQuery) {
        DataTable<Certificate> table =  certificateService.queryCertificatePage(pageQuery);
        return certificateWrapper.wrap(table, CertificateVO.Certificate.class);
    }

}