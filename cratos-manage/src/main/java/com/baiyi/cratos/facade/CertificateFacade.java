package com.baiyi.cratos.facade;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.Certificate;
import com.baiyi.cratos.domain.param.http.certificate.CertificateParam;
import com.baiyi.cratos.domain.view.certificate.CertificateVO;

/**
 * @Author baiyi
 * @Date 2024/1/3 11:27
 * @Version 1.0
 */
public interface CertificateFacade extends HasSetValid {

    Certificate addCertificate(CertificateParam.AddCertificate addCertificate);

    void updateCertificate(CertificateParam.UpdateCertificate updateCertificate);

    DataTable<CertificateVO.Certificate> queryCertificatePage(CertificateParam.CertificatePageQuery pageQuery);

    void deleteByCertificateId(String certificateId);

    void deleteById(int id);

}