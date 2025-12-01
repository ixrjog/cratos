package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.Certificate;
import com.baiyi.cratos.domain.param.http.certificate.CertificateParam;
import com.baiyi.cratos.mapper.CertificateMapper;
import com.baiyi.cratos.service.base.BaseValidService;
import com.baiyi.cratos.service.base.SupportBusinessService;

import java.util.Date;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/1/3 11:11
 * @Version 1.0
 */
public interface CertificateService extends BaseValidService<Certificate, CertificateMapper>, SupportBusinessService {

    void deleteByCertificateId(String certificateId);

    DataTable<Certificate> queryCertificatePage(CertificateParam.CertificatePageQuery pageQuery);

    List<Certificate> queryByLessThanExpiry(Date date);

    List<String> getCertificateNameOptions(CertificateParam.GetCertificateNameOptions getCertificateNameOptions);

    List<Certificate> queryByName(String name);

}