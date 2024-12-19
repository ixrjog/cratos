package com.baiyi.cratos.domain.param.http.eds;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/19 11:39
 * &#064;Version 1.0
 */
public class EdsKubernetesNodeParam {

    @Data
    @Builder
    @Schema
    public static class QueryKubernetesNodeDetails {
        @NotBlank
        private String instanceName;
    }

}
