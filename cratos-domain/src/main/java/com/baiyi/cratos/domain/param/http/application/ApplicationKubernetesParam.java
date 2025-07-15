package com.baiyi.cratos.domain.param.http.application;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/20 11:28
 * &#064;Version 1.0
 */
public class ApplicationKubernetesParam {

    @Data
    @Builder
    @Schema
    public static class QueryKubernetesDetails {
        @NotBlank
        private String applicationName;
        @NotBlank
        private String namespace;
        @Schema(description = "Resource Name")
        private String name;
    }

    @Data
    @Builder
    @Schema
    public static class QueryKubernetesDeploymentOptions {
        @NotBlank
        private String applicationName;
        @NotBlank
        private String namespace;
    }

    @Data
    @Builder
    @Schema
    public static class QueryKubernetesDeploymentImageVersion {
        private String applicationName;
        @NotBlank
        private String image;
    }

    @Data
    @Builder
    @Schema
    public static class DeleteApplicationResourceKubernetesDeploymentPod {
        @NotBlank
        private String applicationName;
        @NotBlank
        private String namespace;
        @NotBlank
        private String deploymentName;
        @NotBlank
        private String podName;
    }

    @Data
    @Builder
    @Schema
    public static class RedeployApplicationResourceKubernetesDeployment {
        @NotBlank
        private String applicationName;
        @NotBlank
        private String namespace;
        @NotBlank
        private String deploymentName;
    }

}
