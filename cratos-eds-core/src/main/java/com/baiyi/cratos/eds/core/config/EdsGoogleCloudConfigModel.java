package com.baiyi.cratos.eds.core.config;

import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.eds.core.config.base.IEdsConfigModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/7/29 上午10:19
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class EdsGoogleCloudConfigModel {

    @Data
    @NoArgsConstructor
    public static class GoogleCloud implements IEdsConfigModel {
        private Project project;
        private EdsInstance edsInstance;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Project {
        private String id;
        private String name;
    }

}