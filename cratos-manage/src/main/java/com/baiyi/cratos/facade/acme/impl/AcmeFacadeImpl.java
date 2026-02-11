package com.baiyi.cratos.facade.acme.impl;

import com.baiyi.cratos.annotation.SetSessionUserToParam;
import com.baiyi.cratos.common.exception.EdsAcmeException;
import com.baiyi.cratos.common.util.ValidationUtils;
import com.baiyi.cratos.domain.generator.AcmeAccount;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.param.http.acme.AcmeAccountParam;
import com.baiyi.cratos.eds.acme.enums.AcmeProviderEnum;
import com.baiyi.cratos.eds.acme.manager.SecureAcmeAccountManager;
import com.baiyi.cratos.eds.acme.model.AcmeModel;
import com.baiyi.cratos.facade.acme.AcmeFacade;
import com.baiyi.cratos.service.acme.AcmeAccountService;
import com.baiyi.cratos.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shredzone.acme4j.exception.AcmeException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/2/9 17:44
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AcmeFacadeImpl implements AcmeFacade {

    private final UserService userService;
    private final AcmeAccountService acmeAccountService;

    @SetSessionUserToParam(desc = "set CreatedBy")
    public void createAcmeAccount(AcmeAccountParam.CreateAccount createAccount) {
        String email = ValidationUtils.isEmail(
                createAccount.getEmail()) ? createAccount.getEmail() : Optional.ofNullable(
                        userService.getByUsername(createAccount.getCreatedBy()))
                .map(User::getEmail)
                .filter(ValidationUtils::isEmail)
                .orElseThrow(() -> new EdsAcmeException("Invalid email."));
        AcmeProviderEnum acmeProviderEnum = AcmeProviderEnum.getByProvider(createAccount.getAcmeProvider());
        try {
            AcmeModel.Account newAccount = SecureAcmeAccountManager.createAccount(email, acmeProviderEnum);
            AcmeAccount acmeAccount = AcmeAccount.builder()
                    .name(createAccount.getName())
                    .acmeServer(newAccount.getAcmeServer())
                    .accountUrl(newAccount.getAccountUrl())
                    .acmeProvider(createAccount.getAcmeProvider())
                    .accountKeyPair(newAccount.getAccountKeyPair())
                    .email(email)
                    .valid(true)
                    .build();
            acmeAccountService.add(acmeAccount);
        } catch (AcmeException | IOException ex) {
            log.error(ex.getMessage());
            EdsAcmeException.runtime(ex.getMessage());
        }
    }

    /**
     *  申请 ACME 证书
     * @param acmeAccount
     * @throws Exception
     */
//    public void issueCertificate(AcmeAccount acmeAccount, int domainId) throws Exception {
//        // 1. 加载账户
//        Account account = SecureAcmeAccountManager.loadAccount(
//                BeanCopierUtils.copyProperties(acmeAccount, AcmeModel.Account.class));
//
//        // 2. 生成域名密钥对
//        KeyPair domainKeyPair = KeyPairUtils.createKeyPair(2048);
//
//        // 3. 创建订单
//        Order order = AcmeCertificateManager.createOrder(account, domains);
//
//        // 4. 完成 DNS 验证
//        completeDnsChallenge(order, dnsProvider);
//
//        // 5. 提交 CSR
//        submitCsr(order, domainKeyPair, domains);
//
//        // 6. 下载证书
//        AcmeCertificate cert = downloadCertificate(order, domainKeyPair);
//
//        // 7. 保存到数据库
//        saveCertificateToDb(cert, domains, acmeAccount.getId());
//
//        return cert;
//    }

}
