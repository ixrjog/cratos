package com.baiyi.cratos.eds.acme.model;

import com.baiyi.cratos.eds.dnsgoogle.enums.DnsRRType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/3/16 13:54
 * &#064;Version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AcmeDnsRecord {
    private String domain;
    @Builder.Default
    private String recordName = "_acme-challenge";
    private String digest;
    @Builder.Default
    private String recordType = DnsRRType.TXT.name();
}
