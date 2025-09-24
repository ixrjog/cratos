package com.baiyi.cratos.shell.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/9/24 10:15
 * &#064;Version 1.0
 */
public class CopyDeploymentSidecarParam {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CopyDeploymentSidecar {

        private String fromInstanceName;
        private String fromNamespace;
        private String fromDeploymentName;
        private String fromContainerName;

        private String toInstanceName;
        private String toNamespace;
        private String toDeploymentName;

    }

}
