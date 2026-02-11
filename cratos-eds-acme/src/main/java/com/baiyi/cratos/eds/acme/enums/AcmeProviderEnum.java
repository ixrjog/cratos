package com.baiyi.cratos.eds.acme.enums;

import lombok.Getter;

/**
 * ACME Provider Enum
 * @Author baiyi
 * @Date 2026/2/9 16:16
 * @Version 1.0
 */
@Getter
public enum AcmeProviderEnum {

    LETSENCRYPT("letsencrypt", "Let's Encrypt", "acme://letsencrypt.org"),
    LETSENCRYPT_STAGING("letsencrypt-staging", "Let's Encrypt Staging", "acme://letsencrypt.org/staging"),
    ZEROSSL("zerossl", "ZeroSSL", "https://acme.zerossl.com/v2/DV90"),
    GOOGLE("google", "Google Trust Services", "https://dv.acme-v02.api.pki.goog/directory"),
    BUYPASS("buypass", "Buypass", "https://api.buypass.com/acme/directory"),
    SSL_COM("ssl.com", "SSL.com", "https://acme.ssl.com/sslcom-dv-rsa");

    private final String provider;
    private final String displayName;
    private final String acmeServer;

    AcmeProviderEnum(String provider, String displayName, String acmeServer) {
        this.provider = provider;
        this.displayName = displayName;
        this.acmeServer = acmeServer;
    }

    public static AcmeProviderEnum getByProvider(String provider) {
        for (AcmeProviderEnum e : values()) {
            if (e.getProvider().equals(provider)) {
                return e;
            }
        }
        return LETSENCRYPT;
    }

}
