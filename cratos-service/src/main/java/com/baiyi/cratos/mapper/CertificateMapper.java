package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.Certificate;
import com.baiyi.cratos.domain.param.http.certificate.CertificateParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface CertificateMapper extends Mapper<Certificate> {

    List<Certificate> queryPageByParam(CertificateParam.CertificatePageQuery pageQuery);

}