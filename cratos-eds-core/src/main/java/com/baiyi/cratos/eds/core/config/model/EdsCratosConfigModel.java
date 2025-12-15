package com.baiyi.cratos.eds.core.config.model;

import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.eds.core.config.base.IEdsConfigModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/5/13 11:26
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class EdsCratosConfigModel {

    @Data
    @NoArgsConstructor
    @Schema
    public static class Cratos implements  IEdsConfigModel {
        private String version;
        private EdsInstance edsInstance;
    }

}
