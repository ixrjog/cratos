package com.baiyi.cratos.eds.acme.manager;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shredzone.acme4j.Account;
import org.shredzone.acme4j.Authorization;
import org.shredzone.acme4j.Order;
import org.shredzone.acme4j.Status;
import org.shredzone.acme4j.challenge.Dns01Challenge;
import org.shredzone.acme4j.exception.AcmeException;
import org.shredzone.acme4j.util.CSRBuilder;

import java.security.KeyPair;
import java.util.List;
import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/2/10 10:16
 * &#064;Version 1.0
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AcmeCertificateManager {

    public static Order createOrder(Account account, List<String> domains) throws AcmeException {
        // 创建订单
        Order order = account.newOrder()
                // ["*.example.com", "example.com"]
                .domains(domains)
                .create();
        log.info("Order created: {}", order.getLocation());
        return order;
    }

    /**
     * 2. 完成域名验证 (Challenge)
     * DNS-01 验证（泛域名必需）
     *
     * @param order
     * @throws Exception
     */
    public void completeDnsChallenge(Order order) throws AcmeException, InterruptedException {
        for (Authorization auth : order.getAuthorizations()) {
            String domain = auth.getIdentifier()
                    .getDomain();
            // 获取 DNS-01 Challenge
            Optional<Dns01Challenge> optionalChallenge = auth.findChallenge(Dns01Challenge.TYPE);
            if (optionalChallenge.isPresent()) {
                Dns01Challenge challenge = optionalChallenge.get();
                String digest = challenge.getDigest();

                log.info("Domain: {}", domain);
                log.info("Add TXT record: _acme-challenge.{} = {}", domain, digest);

                //  TODO 添加 DNS TXT 记录
                // addDnsTxtRecord("_acme-challenge." + domain, digest);
                // 等待 DNS 传播
                Thread.sleep(30000);

                // 触发验证
                challenge.trigger();

                // 等待验证完成
                int attempts = 10;
                while (auth.getStatus() != Status.VALID && attempts-- > 0) {
                    Thread.sleep(3000);
                    // 更新 Authorization 状态
                    auth.fetch();
                }
                if (auth.getStatus() != Status.VALID) {
                    throw new AcmeException("Challenge failed: " + auth.getStatus());
                }
                log.info("Challenge completed for domain: {}", domain);
            }
        }
    }

    /**
     * 3. 生成 CSR 并提交
     *
     * @param order
     * @param domainKeyPair
     * @param domains
     * @throws Exception
     */
    public void submitCsr(Order order, KeyPair domainKeyPair, List<String> domains) throws Exception {
        // 生成 CSR
        CSRBuilder csrBuilder = new CSRBuilder();
        csrBuilder.addDomains(domains);
        csrBuilder.sign(domainKeyPair);

        // 提交 CSR
        order.execute(csrBuilder.getEncoded());

        // 等待证书签发
        int attempts = 10;
        while (order.getStatus() != Status.VALID && attempts-- > 0) {
            Thread.sleep(3000);
            order.fetch();
        }

        if (order.getStatus() != Status.VALID) {
            throw new Exception("Order failed: " + order.getStatus());
        }
    }


    /**
     * 4. 下载证书
     *
     * @param order
     * @param domainKeyPair
     * @return
     * @throws Exception
     */
//    public AcmeCertificate downloadCertificate(Order order, KeyPair domainKeyPair) throws Exception {
//        // 获取证书
//        Certificate certificate = order.getCertificate();
//
//        // 转换为 PEM 字符串
//        StringWriter certWriter = new StringWriter();
//        certificate.writeCertificate(certWriter);
//        String certPem = certWriter.toString();
//
//        // 获取证书链
//        List<X509Certificate> chain = certificate.getCertificateChain();
//
//        // 转换私钥为 PEM
//        StringWriter keyWriter = new StringWriter();
//        KeyPairUtils.writeKeyPair(domainKeyPair, keyWriter);
//        String privateKeyPem = keyWriter.toString();
//
//        // 解析证书有效期
//        X509Certificate x509Cert = certificate.getCertificate();
//        Date notBefore = x509Cert.getNotBefore();
//        Date notAfter = x509Cert.getNotAfter();
//
//        return AcmeCertificate.builder()
//                .certificate(certPem)
//                .certificateChain(chainToPem(chain))
//                .privateKey(privateKeyPem)
//                .issueDate(notBefore)
//                .expireDate(notAfter)
//                .build();
//    }


    /**
     * 5. 完整流程
     *
     * @param acmeAccount
     * @param domains
     * @param dnsProvider
     * @return
     * @throws Exception
     */
//    public AcmeCertificate issueCertificate(AcmeAccount acmeAccount, List<String> domains,
//                                            DnsProvider dnsProvider) throws Exception {
//        // 1. 加载账户
//        Account account = loadAccount(acmeAccount);
//
//        // 2. 生成域名密钥对
//        KeyPair domainKeyPair = KeyPairUtils.createKeyPair(2048);
//
//        // 3. 创建订单
//        Order order = createOrder(account, domains);
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
