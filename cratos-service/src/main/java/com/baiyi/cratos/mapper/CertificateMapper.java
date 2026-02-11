package com.baiyi.cratos.mapper;

import com.baiyi.cratos.domain.generator.Certificate;
import com.baiyi.cratos.domain.param.http.certificate.CertificateParam;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface CertificateMapper extends Mapper<Certificate> {

    List<Certificate> queryPageByParam(CertificateParam.CertificatePageQuery pageQuery);

    List<String> getCertificateNameOptions(
            CertificateParam.GetCertificateNameOptions getCertificateNameOptions);

}