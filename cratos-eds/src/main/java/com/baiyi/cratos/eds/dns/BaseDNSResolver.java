package com.baiyi.cratos.eds.dns;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/12/11 16:11
 * &#064;Version 1.0
 */
public abstract class BaseDNSResolver implements DNSResolver {

    protected String toFQDN(String domainName) {
        if (domainName == null || domainName.isEmpty()) {
            return domainName;
        }
        return domainName.endsWith(".") ? domainName : domainName + ".";
    }

}
