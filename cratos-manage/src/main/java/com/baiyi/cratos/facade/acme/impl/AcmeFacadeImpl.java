package com.baiyi.cratos.facade.acme.impl;

import com.baiyi.cratos.annotation.SetSessionUserToParam;
import com.baiyi.cratos.common.exception.EdsAcmeException;
import com.baiyi.cratos.common.exception.TrafficRouteException;
import com.baiyi.cratos.common.util.ValidationUtils;
import com.baiyi.cratos.domain.generator.*;
import com.baiyi.cratos.domain.model.AcmeDNS;
import com.baiyi.cratos.domain.param.http.acme.AcmeAccountParam;
import com.baiyi.cratos.domain.param.http.acme.AcmeDomainParam;
import com.baiyi.cratos.domain.util.BeanCopierUtils;
import com.baiyi.cratos.domain.util.JSONUtils;
import com.baiyi.cratos.eds.acme.AcmeDNSResolver;
import com.baiyi.cratos.eds.acme.AcmeDNSResolverFactory;
import com.baiyi.cratos.eds.acme.enums.AcmeProviderEnum;
import com.baiyi.cratos.eds.acme.manager.AcmeCertificateManager;
import com.baiyi.cratos.eds.acme.manager.SecureAcmeAccountManager;
import com.baiyi.cratos.eds.acme.model.AcmeModel;
import com.baiyi.cratos.eds.dns.DNSResolver;
import com.baiyi.cratos.eds.dns.DNSResolverFactory;
import com.baiyi.cratos.facade.acme.AcmeFacade;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.service.acme.AcmeAccountService;
import com.baiyi.cratos.service.acme.AcmeCertificateService;
import com.baiyi.cratos.service.acme.AcmeDomainService;
import com.baiyi.cratos.service.acme.AcmeOrderService;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shredzone.acme4j.*;
import org.shredzone.acme4j.Certificate;
import org.shredzone.acme4j.challenge.Dns01Challenge;
import org.shredzone.acme4j.exception.AcmeException;
import org.shredzone.acme4j.util.CSRBuilder;
import org.shredzone.acme4j.util.KeyPairUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private final EdsInstanceService edsInstanceService;
    private final AcmeDomainService acmeDomainService;
    private final AcmeOrderService acmeOrderService;
    private final AcmeCertificateService acmeCertificateService;

    @Override
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

    @Override
    public void addAcmeDomain(AcmeDomainParam.AddDomain addDomain) {
        AcmeDomain acmeDomain = addDomain.toTarget();
        acmeDomain.setValid(true);
        EdsInstance edsInstance = Optional.ofNullable(edsInstanceService.getById(acmeDomain.getDnsResolverInstanceId()))
                .orElseThrow(() -> new TrafficRouteException("Eds instance id not found."));
        DNSResolver dnsResolver = DNSResolverFactory.getDNSResolver(edsInstance.getEdsType());
        String zoneId = dnsResolver.getZoneId(acmeDomain);
        acmeDomain.setZoneId(zoneId);
        String domains = Stream.of("*." + addDomain.getDomain(), addDomain.getDomain())
                .sorted()
                .collect(Collectors.joining(","));
        acmeDomain.setDomains(domains);
        acmeDomainService.add(acmeDomain);
    }

    /**
     * 3. 提交 CSR
     *
     * @param acmeDomain
     * @param acmeOrder
     * @param order
     * @throws Exception
     */
    private void submitCsr(AcmeDomain acmeDomain, AcmeOrder acmeOrder, Order order) throws Exception {
        List<String> domains = Arrays.asList(acmeDomain.getDomains()
                                                     .split(","));
        // 读取域名密钥对（确保已解密）
        String keyPairPem = acmeOrder.getDomainKeyPair();
        if (keyPairPem == null || keyPairPem.isEmpty()) {
            EdsAcmeException.runtime("Domain key pair is empty.");
        }
        // log.info("Key pair PEM length: {}", keyPairPem.length());
        // log.info("Key pair PEM preview: {}", keyPairPem.substring(0, Math.min(100, keyPairPem.length())));
        KeyPair domainKeyPair;
        try (StringReader sr = new StringReader(keyPairPem)) {
            domainKeyPair = KeyPairUtils.readKeyPair(sr);
        } catch (IOException e) {
            log.error("Failed to read key pair: {}", e.getMessage());
            // log.error("Key pair content: {}", keyPairPem);
            throw new EdsAcmeException("Invalid key pair format: " + e.getMessage());
        }
        CSRBuilder csrb = new CSRBuilder();
        csrb.addDomains(domains);
        csrb.sign(domainKeyPair);
        order.execute(csrb.getEncoded());
    }

    public void completeDnsChallenge(Order order, AcmeOrder acmeOrder) throws AcmeException, InterruptedException {
        // 2. 触发验证
        for (Authorization auth : order.getAuthorizations()) {
            Dns01Challenge challenge = (Dns01Challenge) auth.findChallenge(Dns01Challenge.TYPE)
                    .orElseThrow(() -> new EdsAcmeException(
                            "DNS-01 challenge not found for domain: " + auth.getIdentifier()
                                    .getDomain()));
            challenge.trigger();
            // 等待验证完成
            int attempts = 10;
            while (challenge.getStatus() != Status.VALID && attempts-- > 0) {
                Thread.sleep(3000);
                challenge.fetch();
            }
            if (challenge.getStatus() != Status.VALID) {
                EdsAcmeException.runtime("Challenge failed: " + challenge.getStatus());
            }
            log.info(
                    "Challenge completed for domain: {}", auth.getIdentifier()
                            .getDomain()
            );
        }
        // 更新 Order 状态
        order.fetch();
        if (order.getStatus() == Status.READY) {
            acmeOrder.setOrderStatus(order.getStatus()
                                             .name());
            acmeOrderService.updateByPrimaryKey(acmeOrder);
        }
    }

    public AcmeCertificate downloadCertificate(Order order, AcmeDomain acmeDomain,
                                               AcmeOrder acmeOrder) throws Exception {
        Certificate certificate = order.getCertificate();
        // 获取证书内容（PEM 格式）
        StringWriter certWriter = new StringWriter();
        certificate.writeCertificate(certWriter);
        String certPem = certWriter.toString();
        // 获取证书链（包含所有证书）
        List<X509Certificate> chain = certificate.getCertificateChain();
        StringBuilder chainBuilder = new StringBuilder();
        for (X509Certificate cert : chain) {
            chainBuilder.append("-----BEGIN CERTIFICATE-----\n");
            chainBuilder.append(Base64.getMimeEncoder(64, "\n".getBytes())
                                        .encodeToString(cert.getEncoded()));
            chainBuilder.append("\n-----END CERTIFICATE-----\n");
        }
        String certChainPem = chainBuilder.toString();
        X509Certificate x509Cert = chain.getFirst(); // 第一个是域名证书
        AcmeCertificate acmeCertificate = AcmeCertificate.builder()
                .accountId(acmeDomain.getAccountId())
                .domainId(acmeDomain.getDomainId())
                .orderId(acmeOrder.getId())
                .domains(acmeDomain.getDomains())
                .certificate(certPem)
                .certificateChain(certChainPem)
                .privateKey(acmeOrder.getDomainKeyPair())
                .notBefore(x509Cert.getNotBefore())
                .notAfter(x509Cert.getNotAfter())
                .valid(true)
                .build();
        acmeCertificateService.add(acmeCertificate);
        return acmeCertificate;
    }

    @Override
    public void issueCertificate(int acmeDomainId) throws Exception {
        AcmeDomain acmeDomain = acmeDomainService.getById(acmeDomainId);
        Order order = createOrderByAcmeDomain(acmeDomainId);
        AcmeOrder acmeOrder = acmeOrderService.getByOrderUrl(order.getLocation()
                                                                     .toString());
        // 1. DNS 验证
        EdsInstance acmeDNSResolverInstance = edsInstanceService.getById(acmeDomain.getDnsResolverInstanceId());
        AcmeDNSResolver acmeDNSResolver = AcmeDNSResolverFactory.getAcmeDNSResolver(
                acmeDNSResolverInstance.getEdsType());
        // 查询冲突的 ACME _acme-challenge记录，有则删除
        AcmeDNS.AcmeChallengeRecord acmeChallengeRecord = acmeDNSResolver.getAcmeChallenge(acmeDomain);
        if (!acmeChallengeRecord.isNoData()) {
            acmeDNSResolver.deleteAcmeChallenge(acmeDomain);
        }
        // 添加 DNS Challenge 记录
        acmeDNSResolver.addOrderChallengeRecords(acmeDomain, order);
        // 等待 DNS 传播
        Thread.sleep(30000);
        // 2. 触发验证
        completeDnsChallenge(order, acmeOrder);
        // 3. 提交 CSR
        submitCsr(acmeDomain, acmeOrder, order);
        // 4. 等待证书签发
        int attempts = 10;
        while (order.getStatus() != Status.VALID && attempts-- > 0) {
            Thread.sleep(3000);
            order.fetch();
        }
        if (order.getStatus() != Status.VALID) {
            EdsAcmeException.runtime("Certificate issuance failed: " + order.getStatus());
        }
        // 5. 下载证书
        AcmeCertificate acmeCertificate = downloadCertificate(order, acmeDomain, acmeOrder);
        // 更新 Order 状态为 VALID
        acmeOrder.setOrderStatus(Status.VALID.name());
        acmeOrder.setCertificateId(acmeCertificate.getId());
        acmeOrderService.updateByPrimaryKey(acmeOrder);
        log.info("Certificate issued successfully for domain: {}", acmeDomain.getDomain());
    }

    private Order createOrderByAcmeDomain(int acmeDomainId) throws Exception {
        AcmeDomain acmeDomain = acmeDomainService.getById(acmeDomainId);
        Account account = getAccount(acmeDomain.getAccountId());
        List<String> domains = Arrays.asList(acmeDomain.getDomains()
                                                     .split(","));
        Order order = AcmeCertificateManager.createOrder(account, domains);
        // 保存 Order 到数据库
        KeyPair domainKeyPair = KeyPairUtils.createKeyPair(2048);
        StringWriter sw = new StringWriter();
        KeyPairUtils.writeKeyPair(domainKeyPair, sw);
        String keyPairStr = sw.toString();

        // 获取 DNS Challenge 记录
        List<Map<String, String>> dnsRecords = new ArrayList<>();
        for (Authorization auth : order.getAuthorizations()) {
            Optional<Dns01Challenge> optionalDnsChallenge = auth.findChallenge(Dns01Challenge.class);
            if (optionalDnsChallenge.isPresent()) {
                Dns01Challenge dns01Challenge = optionalDnsChallenge.get();
                String domain = auth.getIdentifier()
                        .getDomain();
                String recordName = "_acme-challenge." + domain.replace("*.", "");
                Map<String, String> record = Maps.newHashMap();
                record.put("domain", domain);
                record.put("recordName", recordName);
                record.put("recordValue", dns01Challenge.getDigest());
                record.put(
                        "challengeUrl", dns01Challenge.getLocation()
                                .toString()
                );
                dnsRecords.add(record);
            }
        }
        String orderUrl = order.getLocation()
                .toString();
        if (acmeOrderService.getByOrderUrl(orderUrl) == null) {
            AcmeOrder acmeOrder = AcmeOrder.builder()
                    .accountId(acmeDomain.getAccountId())
                    .domainId(acmeDomainId)
                    .orderUrl(orderUrl)
                    .orderStatus(order.getStatus()
                                         .name())
                    .expires(order.getExpires()
                                     .map(Date::from)
                                     .orElse(null))
                    .domains(acmeDomain.getDomains())
                    .dnsChallengeRecords(JSONUtils.writeValueAsString(dnsRecords))
                    .domainKeyPair(keyPairStr)
                    .build();
            acmeOrderService.add(acmeOrder);
        }
        return order;
    }

    private Account getAccount(int acmeAccountId) throws Exception {
        AcmeAccount acmeAccount = acmeAccountService.getById(acmeAccountId);
        return SecureAcmeAccountManager.loadAccount(
                BeanCopierUtils.copyProperties(acmeAccount, AcmeModel.Account.class));
    }

}
