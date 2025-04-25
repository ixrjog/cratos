package com.baiyi.cratos.eds.opscloud.param;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/25 13:34
 * &#064;Version 1.0
 */
public class OcLeoParam {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QueryBuildImageVersion {
        @NotBlank
        private String image;
    }

}
