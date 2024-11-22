package com.baiyi.cratos.domain.param.socket.kubernetes;

import com.baiyi.cratos.domain.param.socket.HasSocketRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/22 14:15
 * &#064;Version 1.0
 */
public class ApplicationKubernetesRequest {

    @Data
    @Builder
    @Schema
    public static class KubernetesDetailsRequest implements HasSocketRequest {
        private String topic;
        private String action;
        @NotBlank
        private String applicationName;
        @NotBlank
        private String namespace;
    }

}
