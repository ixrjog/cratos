package com.baiyi.cratos.eds.acme.manager;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shredzone.acme4j.Account;
import org.shredzone.acme4j.Order;
import org.shredzone.acme4j.exception.AcmeException;

import java.util.List;

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

}
