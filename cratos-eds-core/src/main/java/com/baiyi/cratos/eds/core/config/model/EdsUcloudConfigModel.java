package com.baiyi.cratos.eds.core.config.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/3/6 10:44
 * &#064;Version 1.0
 */
public class EdsUcloudConfigModel {

    @Data
    @NoArgsConstructor
    @Schema
    public static class Cred {
        private String uid;
        private String name;
        private String company;
        private String publicKey;
        private String privateKey;
    }

}
