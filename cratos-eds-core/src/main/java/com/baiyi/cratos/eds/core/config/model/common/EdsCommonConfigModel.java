package com.baiyi.cratos.eds.core.config.model.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/2/27 18:25
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class EdsCommonConfigModel {

    @Data
    @NoArgsConstructor
    @Schema
    public static class ACME {
        private List<String> domains;
    }

}
