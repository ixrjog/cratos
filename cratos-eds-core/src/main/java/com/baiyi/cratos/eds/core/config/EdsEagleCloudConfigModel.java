package com.baiyi.cratos.eds.core.config;

import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.eds.core.config.base.IEdsConfigModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/9/29 15:20
 * &#064;Version 1.0
 */
public class EdsEagleCloudConfigModel {

    @Data
    @NoArgsConstructor
    @Schema
    public static class Sase implements IEdsConfigModel {
        private Cred cred;
        private EdsInstance edsInstance;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Cred {
        private String token;
    }

}
