package com.baiyi.cratos.domain.param.http.application;

import com.baiyi.cratos.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/24 10:24
 * &#064;Version 1.0
 */
public class ApplicationActuatorParam {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class ApplicationActuatorPageQuery extends PageParam {
        private String applicationName;
        private String namespace;
        private String framework;
        private Boolean standard;
    }

}
