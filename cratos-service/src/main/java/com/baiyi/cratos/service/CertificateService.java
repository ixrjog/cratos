package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.Certificate;
import com.baiyi.cratos.domain.param.certificate.CertificateParam;
import com.baiyi.cratos.service.base.BaseService;

/**
 * @Author baiyi
 * @Date 2024/1/3 11:11
 * @Version 1.0
 */
public interface CertificateService extends BaseService<Certificate> {

    void deleteByCertificateId(String certificateId);

    DataTable<Certificate> queryCertificatePage(CertificateParam.CertificatePageQuery pageQuery);

}