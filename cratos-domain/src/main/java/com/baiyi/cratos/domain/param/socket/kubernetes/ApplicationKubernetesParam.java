package com.baiyi.cratos.domain.param.socket.kubernetes;

import com.baiyi.cratos.domain.param.socket.HasSocketRequest;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/22 14:15
 * &#064;Version 1.0
 */
public class ApplicationKubernetesParam {

    public static KubernetesDetailsRequest loadAs(String message) throws JsonSyntaxException {
        return new GsonBuilder().create()
                .fromJson(message, KubernetesDetailsRequest.class);
    }

    @Data
    @Builder
    public static class KubernetesDetailsRequest implements HasSocketRequest {
        private String topic;
        private String action;
        //@NotBlank
        private String applicationName;
        //@NotBlank
        private String namespace;
        private String name;
        // Watch container logs
        private List<DeploymentRequest> deployments;
    }

    @Data
    @Builder
    public static class KubernetesContainerTerminalResize implements HasSocketRequest {
        private String topic;
        private String action;
        //@NotBlank
        private String applicationName;
        //@NotBlank
        private String namespace;
        private List<DeploymentRequest> deployments;
    }

    @Data
    @Builder
    public static class KubernetesExecRequest implements HasSocketRequest {
        private String topic;
        private String action;
    }

    @Data
    @Builder
    public static class DeploymentRequest {
        private String kubernetesClusterName;
        // deployment name
        private String name;
        private List<PodRequest> pods;
    }

    @Data
    @Builder
    public static class PodRequest {
        private String instanceId;
        private String name;
        private String namespace;
        private ContainerRequest container;
    }

    @Data
    @Builder
    public static class ContainerRequest {
        private String name;
    }

}
