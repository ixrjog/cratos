package com.baiyi.cratos.eds.googlecloud.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/7/29 下午5:23
 * &#064;Version 1.0
 */
public class GoogleCertificateModel {

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Certificate {
        private List<String> dnsNames;
        private String name;
        private String key;
        private Date createdTime;
        private Date expiredTime;
        private String description;
    }

    public static Certificate toCertificate(com.google.cloud.certificatemanager.v1.Certificate certificate) {
        Optional<String> optionalDns = certificate.getSanDnsnamesList()
                                .stream()
                                .filter(e -> e.startsWith("*."))
                              .findFirst();
                 final String name = optionalDns.orElseGet(() -> certificate.getSanDnsnames(0));
        return Certificate.builder()
                .dnsNames(certificate.getSanDnsnamesList()
                        .stream()
                        .toList())
                .name(name)
                .key(certificate.getName())
                .createdTime(new Date(certificate.getCreateTime().getSeconds() * 1000))
                .expiredTime(new Date(certificate.getExpireTime().getSeconds() * 1000))
                .description(certificate.getDescription())
                .build();
    }

}
