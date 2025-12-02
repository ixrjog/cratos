package com.baiyi.cratos.domain.util.dnsgoogle.enums;

import lombok.Getter;

/**
 * @Author baiyi
 * @Date 2025/12/2 16:21
 * @Version 1.0
 */
@Getter
public enum DnsTypes {

    A(1),
    NS(2),
    CNAME(5),
    SOA(6),
    PTR(12),
    MX(15),
    TXT(16),
    AAAA(28),
    SRV(33),
    NAPTR(35),
    DS(43),
    SSHFP(44),
    RRSIG(46),
    NSEC(47),
    DNSKEY(48),
    NSEC3(50),
    TLSA(52),
    CAA(257);

    private final int type;

    DnsTypes(int type) {
        this.type = type;
    }

}
