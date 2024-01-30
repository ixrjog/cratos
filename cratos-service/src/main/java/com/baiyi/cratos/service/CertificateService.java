package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.Certificate;
import com.baiyi.cratos.domain.param.certificate.CertificateParam;
import com.baiyi.cratos.mapper.CertificateMapper;
import com.baiyi.cratos.service.base.BaseValidService;
import com.baiyi.cratos.service.base.SupportBusinessTagService;

/**
 * @Author baiyi
 * @Date 2024/1/3 11:11
 * @Version 1.0
 */
public interface CertificateService extends BaseValidService<Certificate, CertificateMapper>, SupportBusinessTagService {

    void deleteByCertificateId(String certificateId);

    DataTable<Certificate> queryCertificatePage(CertificateParam.CertificatePageQuery pageQuery);

}