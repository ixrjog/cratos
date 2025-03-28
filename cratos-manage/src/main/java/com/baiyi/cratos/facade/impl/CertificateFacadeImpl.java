package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.annotation.BindAssetsAfterImport;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.Certificate;
import com.baiyi.cratos.domain.param.http.certificate.CertificateParam;
import com.baiyi.cratos.domain.view.certificate.CertificateVO;
import com.baiyi.cratos.facade.CertificateFacade;
import com.baiyi.cratos.service.CertificateService;
import com.baiyi.cratos.service.base.BaseValidService;
import com.baiyi.cratos.wrapper.CertificateWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2024/1/3 11:27
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CertificateFacadeImpl implements CertificateFacade {

    private final CertificateService certificateService;
    private final CertificateWrapper certificateWrapper;

    @Override
    @BindAssetsAfterImport
    public Certificate addCertificate(CertificateParam.AddCertificate addCertificate) {
        Certificate certificate = addCertificate.toTarget();
        certificateService.add(certificate);
        return certificate;
    }

    @Override
    public void updateCertificate(CertificateParam.UpdateCertificate updateCertificate) {
        certificateService.updateByPrimaryKey(updateCertificate.toTarget());
    }

    @Override
    public DataTable<CertificateVO.Certificate> queryCertificatePage(CertificateParam.CertificatePageQuery pageQuery) {
        DataTable<Certificate> table = certificateService.queryCertificatePage(pageQuery);
        return certificateWrapper.wrapToTarget(table);
    }

    @Override
    public void deleteByCertificateId(String certificateId) {
        certificateService.deleteByCertificateId(certificateId);
    }

    @Override
    public void deleteById(int id) {
        certificateService.deleteById(id);
    }

    @Override
    public BaseValidService<?, ?> getValidService() {
        return certificateService;
    }
}