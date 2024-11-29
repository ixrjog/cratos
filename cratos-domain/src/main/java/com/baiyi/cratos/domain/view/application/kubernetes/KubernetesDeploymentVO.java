package com.baiyi.cratos.domain.view.application.kubernetes;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/21 11:35
 * &#064;Version 1.0
 */
public class KubernetesDeploymentVO {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class Deployment implements KubernetesCommonVO.HasKubernetesCluster, Serializable {
        @Serial
        private static final long serialVersionUID = 9137044441466358774L;
        private KubernetesCommonVO.KubernetesCluster kubernetesCluster;
        private KubernetesCommonVO.Metadata metadata;
        private DeploymentSpec spec;
        private List<KubernetesPodVO.Pod> pods;
        @Schema(description = "拓扑")
        private KubernetesCommonVO.TopologyDetails topologyDetails;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class DeploymentSpec implements Serializable {
        @Serial
        private static final long serialVersionUID = 4549992231198483582L;
        private Integer replicas;
        // 发布策略
        private DeploymentStrategy strategy;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class DeploymentStrategy implements Serializable {
        @Serial
        private static final long serialVersionUID = -873459316958896176L;
        public static final DeploymentStrategy EMPTY = DeploymentStrategy.builder()
                .build();
        private String type;
        private RollingUpdateDeployment rollingUpdate;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class RollingUpdateDeployment implements Serializable {
        @Serial
        private static final long serialVersionUID = 8099732330091548272L;
        private String maxSurge;
        private String maxUnavailable;
    }

}
