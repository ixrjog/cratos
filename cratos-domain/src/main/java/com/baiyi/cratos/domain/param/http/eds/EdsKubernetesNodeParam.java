package com.baiyi.cratos.domain.param.http.eds;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/19 11:39
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class EdsKubernetesNodeParam {

    @Data
    @Builder
    @Schema
    public static class QueryEdsKubernetesNodeDetails {
        @NotBlank
        private String instanceName;
    }

}
