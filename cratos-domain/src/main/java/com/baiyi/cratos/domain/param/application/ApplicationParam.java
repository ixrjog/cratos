package com.baiyi.cratos.domain.param.application;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/20 上午10:49
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class ApplicationParam {

    @Data
    @Schema
    public static class QueryApplicationDeploy {
        @NotNull
        private Integer applicationId;
        @NotNull
        private Integer envType;
    }

}
