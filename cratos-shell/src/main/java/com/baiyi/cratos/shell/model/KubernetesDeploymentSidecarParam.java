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
public class KubernetesDeploymentSidecarParam {

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

        public static KubernetesDeploymentSidecarParam.CopyDeploymentSidecar parse(String from,
                                                                                   String to) throws IllegalArgumentException {
            String[] fromParts = from.split(":");
            if (fromParts.length != 4) {
                throw new IllegalArgumentException(
                        "Invalid 'from' parameter format. Expected format: {EdsKubernetesInstanceName}:{Namespace}:{Deployment}:{Container}");

            }
            String[] toParts = to.split(":");
            if (toParts.length != 3) {
                throw new IllegalArgumentException(
                        "Invalid 'to' parameter format. Expected format: {EdsKubernetesInstanceName}:{Namespace}:{Deployment}");
            }
            return KubernetesDeploymentSidecarParam.CopyDeploymentSidecar.builder()
                    .fromInstanceName(fromParts[0])
                    .fromNamespace(fromParts[1])
                    .fromDeploymentName(fromParts[2])
                    .fromContainerName(fromParts[3])
                    .toInstanceName(toParts[0])
                    .toNamespace(toParts[1])
                    .toDeploymentName(toParts[2])
                    .build();
        }

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RemoveDeploymentSidecar {

        private String instanceName;
        private String namespace;
        private String deploymentName;
        private String containerName;

        public static KubernetesDeploymentSidecarParam.RemoveDeploymentSidecar parse(
                String target) throws IllegalArgumentException {
            String[] targetParts = target.split(":");
            if (targetParts.length != 4) {
                throw new IllegalArgumentException(
                        "Invalid 'from' parameter format. Expected format: {EdsKubernetesInstanceName}:{Namespace}:{Deployment}:{Container}");

            }
            return KubernetesDeploymentSidecarParam.RemoveDeploymentSidecar.builder()
                    .instanceName(targetParts[0])
                    .namespace(targetParts[1])
                    .deploymentName(targetParts[2])
                    .containerName(targetParts[3])
                    .build();
        }
    }

}
