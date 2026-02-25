package com.baiyi.cratos.facade;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.domain.generator.AcmeCertificate;
import com.baiyi.cratos.domain.generator.AcmeOrder;
import com.baiyi.cratos.domain.param.http.acme.AcmeAccountParam;
import com.baiyi.cratos.domain.param.http.acme.AcmeDomainParam;
import com.baiyi.cratos.eds.acme.enums.AcmeProviderEnum;
import com.baiyi.cratos.facade.acme.AcmeFacade;
import com.baiyi.cratos.service.acme.AcmeCertificateService;
import com.baiyi.cratos.service.acme.AcmeOrderService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/2/10 09:55
 * &#064;Version 1.0
 */
public class AcmeFacadeTest extends BaseUnit {

    @Resource
    private AcmeFacade acmeFacade;

    @Autowired
    private AcmeOrderService acmeOrderService;

    @Autowired
    private AcmeCertificateService acmeCertificateService;

    @Test
    void test1() {
        AcmeAccountParam.CreateAccount createAccount = AcmeAccountParam.CreateAccount.builder()
                .name("baiyitest")
                .email("jian.liang@palmapy-inc.com")
                .acmeProvider(AcmeProviderEnum.LETSENCRYPT.getProvider())
                .build();
        acmeFacade.createAcmeAccount(createAccount);
    }

    @Test
    void test2() {
        // easeid.ai
        AcmeDomainParam.AddDomain addDomain = AcmeDomainParam.AddDomain.builder()
                .name("测试")
                .domainId(95)
                .domain("easeid.ai")
                .accountId(1)
                .dnsResolverInstanceId(94)
                .build();
        acmeFacade.addAcmeDomain(addDomain);
    }

    @Test
    void test3() throws Exception {
        acmeFacade.issueCertificate(2);
    }

    @Test
    void test4() {

        AcmeOrder acmeOrder = acmeOrderService.getById(1);
        System.out.println(acmeOrder);
    }

    @Test
    void test5() {
        AcmeCertificate acmeCertificate =  acmeCertificateService.getById(2);

        System.out.println("Certificate:");

        System.out.println(acmeCertificate.getCertificate());

        System.out.println("CertificateChain:");
        System.out.println(acmeCertificate.getCertificateChain());
        System.out.println("Key:");
        System.out.println(acmeCertificate.getPrivateKey());
    }

}
