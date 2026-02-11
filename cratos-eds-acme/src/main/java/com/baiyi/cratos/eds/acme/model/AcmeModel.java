package com.baiyi.cratos.eds.acme.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/2/9 17:14
 * &#064;Version 1.0
 */
public class AcmeModel {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Account implements Serializable {
        @Serial
        private static final long serialVersionUID = 3663856752226430120L;
        private String email;
        private String acmeProvider;
        private String accountUrl;
        private String acmeServer;
        private String accountKeyPair;
    }

}
