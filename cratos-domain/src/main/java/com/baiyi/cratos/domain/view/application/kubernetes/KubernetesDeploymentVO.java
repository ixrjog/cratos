package com.baiyi.cratos.domain.view.application.kubernetes;

import com.baiyi.cratos.domain.generator.Env;
import com.baiyi.cratos.domain.view.application.kubernetes.common.KubernetesCommonVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

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

        public static final Deployment INVALID = Deployment.builder()
                .build();

        private KubernetesCommonVO.KubernetesCluster kubernetesCluster;
        private KubernetesCommonVO.Metadata metadata;
        private DeploymentSpec spec;
        private List<KubernetesPodVO.Pod> pods;
        @Schema(description = "拓扑")
        private KubernetesCommonVO.TopologyDetails topologyDetails;
        @Schema(description = "属性")
        private Map<String, String> attributes;
        private Env env;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(type = "deployment.spec")
    public static class DeploymentSpec implements Serializable {
        @Serial
        private static final long serialVersionUID = 4549992231198483582L;
        private Integer replicas;
        // 发布策略
        private DeploymentStrategy strategy;
        private SpecTemplate template;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(type = "deployment.spec.template")
    public static class SpecTemplate implements Serializable {
        @Serial
        private static final long serialVersionUID = 6533620123953724215L;
        private TemplateSpec spec;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class TemplateSpec implements Serializable {
        @Serial
        private static final long serialVersionUID = -7381336994086390246L;
        private List<TemplateSpecContainer> containers;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(type = "deployment.spec.template.spec.containers")
    public static class TemplateSpecContainer implements Serializable {
        @Serial
        private static final long serialVersionUID = 6005762108817823260L;
        @Schema(description = "Main Container")
        private Boolean main;
        private String name;
        private String image;
        private KubernetesDeploymentVO.ContainerResources resources;

        public int getSeq() {
            return main ? 0 : 1;
        }
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(type = "deployment.strategy")
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
    @Schema(type = "deployment.rollingUpdate")
    public static class RollingUpdateDeployment implements Serializable {
        @Serial
        private static final long serialVersionUID = 8099732330091548272L;
        private String maxSurge;
        private String maxUnavailable;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(type = "deployment.spec.template.spec.containers.resources")
    public static class ContainerResources implements Serializable {
        @Serial
        private static final long serialVersionUID = -8365479765276925656L;
        private Map<String, Quantity> limits;
        private Map<String, Quantity> requests;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(type = "deployment.spec.template.spec.containers.resources.quantity")
    public static class Quantity implements Serializable {
        @Serial
        private static final long serialVersionUID = 3726795430810844718L;
        private String amount;
        private String format;
    }

}
