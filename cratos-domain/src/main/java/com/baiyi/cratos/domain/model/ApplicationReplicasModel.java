package com.baiyi.cratos.domain.model;

import com.baiyi.cratos.domain.view.application.ApplicationVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/29 11:30
 * &#064;Version 1.0
 */
public class ApplicationReplicasModel {

    @Data
    @SuperBuilder(toBuilder = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class ApplicationConfigurationChange implements Serializable {
        @Serial
        private static final long serialVersionUID = -1696328255074876974L;
        private ApplicationVO.Application application;
        private String namespace;
        private ApplicationConfig config;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ApplicationConfig implements Serializable {
        @Serial
        private static final long serialVersionUID = -4336675028983546304L;
        private Integer currentReplicas;
        private Integer expectedReplicas;
    }

}
