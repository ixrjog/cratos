package com.baiyi.cratos.domain.param.eds;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/8/12 下午3:36
 * &#064;Version 1.0
 */
public class EdsBusinessParam {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class KubernetesInstanceResourceQuery {
        @Schema(description = "Eds Instance ID")
        @NotNull
        private Integer instanceId;
        @Schema(description = "可选参数")
        private String env;
        @NotBlank
        private String appName;
    }

}
