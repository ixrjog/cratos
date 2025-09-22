package com.baiyi.cratos.domain.view.application.kubernetes;

import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.view.application.kubernetes.common.KubernetesCommonVO;
import com.baiyi.cratos.domain.view.env.EnvVO;
import com.baiyi.cratos.domain.view.tag.BusinessTagVO;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

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
    public static class Deployment implements KubernetesCommonVO.HasKubernetesCluster, Comparable<Deployment>, BusinessTagVO.HasBusinessTags, EnvVO.HasEnv, Serializable {
        @Serial
        private static final long serialVersionUID = 9137044441466358774L;

        public static final Deployment INVALID = Deployment.builder()
                .build();

        private static final BusinessTypeEnum BUSINESS_TYPE_ENUM = BusinessTypeEnum.EDS_ASSET;
        private Integer assetId;
        private KubernetesCommonVO.KubernetesCluster kubernetesCluster;
        private KubernetesCommonVO.Metadata metadata;
        private DeploymentSpec spec;
        private List<KubernetesPodVO.Pod> pods;
        @Schema(description = "拓扑")
        private KubernetesCommonVO.TopologyDetails topologyDetails;
        @Schema(description = "属性")
        private Map<String, String> attributes;
        private EnvVO.Env env;
        private String envName;
        @Schema(description = "Business Tags")
        private List<BusinessTagVO.BusinessTag> businessTags;

        @Override
        public Integer getBusinessId() {
            return this.assetId;
        }

        @Override
        public int compareTo(Deployment o) {
            return this.metadata.getName()
                    .compareTo(o.metadata.getName());
        }

        @Override
        public String getBusinessType() {
            return BUSINESS_TYPE_ENUM.name();
        }

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
        private Lifecycle lifecycle;
        private Probe livenessProbe;
        private Probe readinessProbe;
        private Probe startupProbe;

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
                .isEmpty(true)
                .build();
        private String type;
        private RollingUpdateDeployment rollingUpdate;
        @Builder.Default
        private boolean isEmpty = false;
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

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Lifecycle implements Serializable {
        public static final Lifecycle EMPTY = Lifecycle.builder()
                .isEmpty(true)
                .build();
        @Serial
        private static final long serialVersionUID = -2552010171914670706L;
        @JsonProperty("postStart")
        private LifecycleHandler postStart;
        @JsonProperty("preStop")
        private LifecycleHandler preStop;
        @Builder.Default
        private boolean isEmpty = false;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LifecycleHandler implements Serializable {
        @Serial
        private static final long serialVersionUID = 8666748233494672828L;
        private ExecAction exec;
        private HTTPGetAction httpGet;
        private SleepAction sleep;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ExecAction implements Serializable {
        @Serial
        private static final long serialVersionUID = 6096511997030766067L;
        private String command;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class HTTPGetAction implements Serializable {
        @Serial
        private static final long serialVersionUID = -837942790581562335L;
        public static final HTTPGetAction EMPTY = HTTPGetAction.builder()
                .isEmpty(true)
                .build();
        private String host;
        @Builder.Default
        private List<HTTPHeader> httpHeaders = Lists.newArrayList();
        private String path;
        private String port;
        private String scheme;
        @Builder.Default
        private boolean isEmpty = false;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class HTTPHeader implements Serializable {
        @Serial
        private static final long serialVersionUID = 1235700388134906731L;
        private String name;
        private String value;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SleepAction implements Serializable {
        @Serial
        private static final long serialVersionUID = -3709439553179190122L;
        private Long seconds;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Probe implements Serializable {
        @Serial
        private static final long serialVersionUID = -2282018100691269848L;
        public static final Probe EMPTY = Probe.builder()
                .isEmpty(true)
                .build();
        private ExecAction exec;
        private Integer failureThreshold;
        //private GRPCAction grpc;
        private HTTPGetAction httpGet;
        private Integer initialDelaySeconds;
        private Integer periodSeconds;
        private Integer successThreshold;
        //private TCPSocketAction tcpSocket;
        private Long terminationGracePeriodSeconds;
        private Integer timeoutSeconds;

        @Builder.Default
        private boolean isEmpty = false;
    }

}
