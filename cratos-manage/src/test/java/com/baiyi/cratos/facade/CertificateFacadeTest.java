package com.baiyi.cratos.facade;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.http.certificate.CertificateParam;
import com.baiyi.cratos.domain.view.certificate.CertificateVO;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

/**
 * @Author baiyi
 * @Date 2024/1/3 13:47
 * @Version 1.0
 */
public class CertificateFacadeTest extends BaseUnit {

    @Resource
    private CertificateFacade certificateFacade;

    @Test
    void pageTest() {
        CertificateParam.CertificatePageQuery pageQuery = CertificateParam.CertificatePageQuery.builder()
                .page(1)
                .length(10)
                .build();
        DataTable<CertificateVO.Certificate> dataTable = certificateFacade.queryCertificatePage(pageQuery);
        System.out.println(pageQuery);
        System.out.println(dataTable);
    }

}