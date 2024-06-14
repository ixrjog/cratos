package com.baiyi.cratos.eds.core.config;

import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.eds.core.config.base.HasRegionModel;
import com.baiyi.cratos.eds.core.config.base.IEdsConfigModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/10 上午10:04
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class EdsHuaweicloudConfigModel {

    @Data
    @NoArgsConstructor
    @Schema
    public static class Huaweicloud implements HasRegionModel, IEdsConfigModel {

        private String version;
        // default
        private String regionId;

        private Set<String> regionIds;

        @Schema(description = "凭据")
        private Cred cred;

        private EdsInstance edsInstance;

    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Cred {

        private String uid;

        private String username;

        private String accessKey;

        private String secretKey;

    }

}
