package com.baiyi.cratos.shell.context;

import com.baiyi.cratos.domain.generator.AcmeDomain;
import com.baiyi.cratos.domain.generator.AcmeOrder;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/2/27 13:51
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AcmeContext {

    private static final ThreadLocal<Map<Integer, AcmeDomain>> DOMAIN_CONTEXT = new ThreadLocal<>();

    public static void setDomainContext(Map<Integer, AcmeDomain> domainContext) {
        DOMAIN_CONTEXT.set(domainContext);
    }

    public static Map<Integer, AcmeDomain> getDomainContext() {
        return DOMAIN_CONTEXT.get();
    }

    private static final ThreadLocal<Map<Integer, AcmeOrder>> ORDER_CONTEXT = new ThreadLocal<>();

    public static void setOrderContext(Map<Integer, AcmeOrder> domainContext) {
        ORDER_CONTEXT.set(domainContext);
    }

    public static Map<Integer, AcmeOrder> getOrderContext() {
        return ORDER_CONTEXT.get();
    }

    public static void remove() {
        DOMAIN_CONTEXT.remove();
        ORDER_CONTEXT.remove();
    }

}
