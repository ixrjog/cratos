package com.baiyi.cratos.eds.core.config.model;

import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.eds.core.config.base.HasRegionsModel;
import com.baiyi.cratos.eds.core.config.base.IEdsConfigModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/7/28 14:35
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class EdsHcsConfigModel {

    @Data
    @NoArgsConstructor
    @Schema
    public static class Hcs implements HasRegionsModel, IEdsConfigModel {
        private String version;
        // default
        private String regionId;
        private Set<String> regionIds;
        @Schema(description = "凭据")
        private Cred cred;
        private ManageOne manageOne;
        private EdsInstance edsInstance;
        private Boolean ignoreSSLVerification;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Cred {
        private String username;
        private String accessKey;
        private String secretKey;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class ManageOne {
        private String consoleUrl;
    }

}
