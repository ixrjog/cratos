package com.baiyi.cratos.domain.view.application.kubernetes;

import com.baiyi.cratos.domain.constant.Global;
import com.baiyi.cratos.domain.view.application.kubernetes.common.KubernetesCommonVO;
import com.baiyi.cratos.domain.view.eds.EdsInstanceVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/19 11:38
 * &#064;Version 1.0
 */
public class KubernetesNodeVO {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class KubernetesNodeDetails implements Serializable {
        @Serial
        private static final long serialVersionUID = 6024185257685366861L;

        public static KubernetesNodeDetails failed(String message) {
            return KubernetesNodeDetails.builder()
                    .message(message)
                    .success(false)
                    .build();
        }

        private Map<String, List<Node>> nodes;
        @Builder.Default
        private boolean success = true;
        private String message;
        private EdsInstanceVO.EdsInstance kubernetesInstance;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class Node implements Serializable {
        @Serial
        private static final long serialVersionUID = 8228997074911369504L;
        private String region;
        private String zone;
        private KubernetesCommonVO.Metadata metadata;
        private NodeStatus status;
        @Schema(description = "属性")
        private Map<String, String> attributes;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class NodeStatus implements Serializable {
        @Serial
        private static final long serialVersionUID = -6345842858862810079L;
        private Map<String, NodeAddress> addresses;
        private NodeSystemInfo nodeInfo;
        // by type
        private Map<String, NodeCondition> conditions;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class NodeAddress implements Serializable {
        @Serial
        private static final long serialVersionUID = -3164941682482477792L;
        private String address;
        private String type;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class NodeSystemInfo implements Serializable {
        @Serial
        private static final long serialVersionUID = -6796123773083522274L;
        private String architecture;
        private String bootID;
        private String containerRuntimeVersion;
        private String kernelVersion;
        private String kubeProxyVersion;
        private String kubeletVersion;
        private String machineID;
        private String operatingSystem;
        private String osImage;
        private String systemUUID;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class NodeCondition implements Serializable {
        @Serial
        private static final long serialVersionUID = -6465086246558577448L;
        @JsonFormat(timezone = "UTC", pattern = Global.ISO8601)
        private Date lastHeartbeatTime;
        @JsonFormat(timezone = "UTC", pattern = Global.ISO8601)
        private Date lastTransitionTime;
        private String message;
        private String reason;
        private String status;
        private String type;
    }

}
