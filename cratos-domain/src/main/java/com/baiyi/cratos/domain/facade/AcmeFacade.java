package com.baiyi.cratos.domain.facade;

import com.baiyi.cratos.domain.generator.AcmeCertificate;
import com.baiyi.cratos.domain.generator.AcmeDomain;
import com.baiyi.cratos.domain.generator.AcmeOrder;
import com.baiyi.cratos.domain.param.http.acme.AcmeAccountParam;
import com.baiyi.cratos.domain.param.http.acme.AcmeDomainParam;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/2/9 17:44
 * &#064;Version 1.0
 */
public interface AcmeFacade {

    void createAcmeAccount(AcmeAccountParam.CreateAccount createAccount);

    void addAcmeDomain(AcmeDomainParam.AddDomain addDomain);

    void issueCertificate(int acmeDomainId) throws Exception;

    void asyncIssueCertificate(int acmeDomainId);

    void resumeOrderIssuance(AcmeOrder acmeOrder) throws Exception;

    void recoverDcvDelegation(AcmeDomain acmeDomain);

    void autoDeployToEdsInstances(AcmeCertificate acmeCertificate);

}
