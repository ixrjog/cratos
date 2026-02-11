package com.baiyi.cratos.eds.acme.manager;

import com.baiyi.cratos.eds.acme.enums.AcmeProviderEnum;
import com.baiyi.cratos.eds.acme.model.AcmeModel;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shredzone.acme4j.Account;
import org.shredzone.acme4j.AccountBuilder;
import org.shredzone.acme4j.Session;
import org.shredzone.acme4j.exception.AcmeException;
import org.shredzone.acme4j.util.KeyPairUtils;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.KeyPair;

/**
 * ACME Account Manager
 *
 * @Author baiyi
 * @Date 2026/2/9 17:05
 * @Version 1.0
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecureAcmeAccountManager {

    /**
     * 创建新的 ACME 账户
     */
    public static AcmeModel.Account createAccount(String email,
                                                  AcmeProviderEnum acmeProvider) throws AcmeException, IOException {
        log.info("Creating new ACME account for email: {}, provider: {}", email, acmeProvider.getProvider());

        // 1. 生成账户密钥对
        KeyPair accountKeyPair = KeyPairUtils.createKeyPair(2048);

        // 2. 获取 ACME Server URL

        String acmeServer = acmeProvider.getAcmeServer();

        // 3. 创建 ACME 账户
        Session session = new Session(acmeServer);
        Account account = new AccountBuilder().addContact("mailto:" + email)
                .agreeToTermsOfService()
                .useKeyPair(accountKeyPair)
                .create(session);

        log.info("ACME account created successfully, URL: {}", account.getLocation());

        // 4. 转换密钥对为 PEM 字符串
        StringWriter sw = new StringWriter();
        KeyPairUtils.writeKeyPair(accountKeyPair, sw);
        String keyPairPem = sw.toString();

        // 5. 构建数据库对象
        return AcmeModel.Account.builder()
                .email(email)
                .acmeProvider(acmeProvider.getProvider())
                .accountUrl(account.getLocation()
                                    .toString())
                .acmeServer(acmeServer)
                .accountKeyPair(keyPairPem)
                .build();
    }

    /**
     * 从数据库加载并登录 ACME 账户
     */
    public static Account loadAccount(AcmeModel.Account acmeAccount) throws Exception {
        log.info("Loading ACME account: {}, provider: {}", acmeAccount.getEmail(), acmeAccount.getAcmeProvider());

        // 1. 转换为 KeyPair 对象
        KeyPair accountKeyPair;
        try (StringReader sr = new StringReader(acmeAccount.getAccountKeyPair())) {
            accountKeyPair = KeyPairUtils.readKeyPair(sr);
        }

        // 2. 获取 ACME Server URL
        String acmeServer = acmeAccount.getAcmeServer();
        if (acmeServer == null || acmeServer.isEmpty()) {
            acmeServer = AcmeProviderEnum.getByProvider(acmeAccount.getAcmeProvider())
                    .getAcmeServer();
        }

        // 3. 创建 Session 并登录
        Session session = new Session(acmeServer);
        Account account = new AccountBuilder().useKeyPair(accountKeyPair)
                .create(session);

        log.info("ACME account loaded successfully, URL: {}", account.getLocation());
        return account;
    }

}
