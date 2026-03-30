package com.baiyi.cratos.domain.facade;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.AcmeDomain;
import com.baiyi.cratos.domain.generator.AcmeOrder;
import com.baiyi.cratos.domain.param.http.acme.AcmeAccountParam;
import com.baiyi.cratos.domain.param.http.acme.AcmeDomainParam;
import com.baiyi.cratos.domain.param.http.acme.AcmeOrderParam;
import com.baiyi.cratos.domain.view.acme.AcmeAccountVO;
import com.baiyi.cratos.domain.view.acme.AcmeCertificateVO;
import com.baiyi.cratos.domain.view.acme.AcmeDomainVO;
import com.baiyi.cratos.domain.view.acme.AcmeOrderVO;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/2/9 17:44
 * &#064;Version 1.0
 */
public interface AcmeFacade {

    /**
     * 创建 ACME 账户
     *
     * @param createAccount
     */
    void createAcmeAccount(AcmeAccountParam.CreateAccount createAccount);

    /**
     * 新增 Domain
     *
     * @param addDomain
     */
    void addAcmeDomain(AcmeDomainParam.AddDomain addDomain);

    /**
     * 申请证书
     *
     * @param acmeDomainId
     */
    void issueCertificate(int acmeDomainId);

    /**
     * 异步申请证书
     *
     * @param acmeDomainId
     */
    void asyncIssueCertificate(int acmeDomainId);

    void resumeOrderIssuance(AcmeOrder acmeOrder) throws Exception;

    /**
     * 恢复 DCV
     *
     * @param acmeDomain
     */
    void recoverDcvDelegation(AcmeDomain acmeDomain);

    /**
     * 证书自动部署到云实例
     *
     * @param acmeCertificateId
     */
    void autoDeployToEdsInstances(int acmeCertificateId);

    DataTable<AcmeAccountVO.Account> queryAccountPage(AcmeAccountParam.AccountPageQuery pageQuery);

    DataTable<AcmeDomainVO.Domain> queryDomainPage(AcmeDomainParam.DomainPageQuery pageQuery);

    DataTable<AcmeOrderVO.Order> queryOrderPage(AcmeOrderParam.OrderPageQuery pageQuery);

    AcmeCertificateVO.Certificate getAcmeCertificateById(int id);

    void updateAcmeDomain(AcmeDomainParam.UpdateDomain updateDomain);

}
