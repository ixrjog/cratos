package com.baiyi.cratos.eds.core.config;

import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.eds.core.config.base.IEdsConfigModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/12 11:27
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class EdsJenkinsConfigModel {

    @Data
    @NoArgsConstructor
    @Schema
    public static class Jenkins implements IEdsConfigModel {
        private String version;
        private String url;
        private String ip;
        private Cred cred;
        private Security security;
        private String name;
        private EdsInstance edsInstance;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Cred {
        private String username;
        private String token;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Security {
        private Boolean crumbFlag;
    }

}
