package com.baiyi.cratos.domain.view.application.kubernetes;

import com.baiyi.cratos.domain.constant.Global;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
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
 * &#064;Date  2024/11/21 11:33
 * &#064;Version 1.0
 */
public class KubernetesPodVO {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class Pod implements Serializable {
        @Serial
        private static final long serialVersionUID = -438695425945757038L;
        // private List<KubernetesContainerVO.ContainerStatus> containers;
        private KubernetesCommonVO.Metadata metadata;
        private PodStatus status;
        private PodSpec spec;
        private List<KubernetesContainerVO.ContainerStatus> containerStatuses;
        @Schema(description = "Kubernetes node, has region & zone")
        private EdsAssetVO.Asset node;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class PodStatus implements Serializable {
        @Serial
        private static final long serialVersionUID = -7125606653208312785L;
        private Map<String, PodCondition> conditions;
        // private List<PodCondition> conditions = new ArrayList();
        // private List<ContainerStatus> containerStatuses = new ArrayList();
        // private List<ContainerStatus> ephemeralContainerStatuses = new ArrayList();
        private String hostIP;
        // private List<HostIP> hostIPs = new ArrayList();
        // private List<ContainerStatus> initContainerStatuses = new ArrayList();
        private String message;
        private String nominatedNodeName;
        private String phase;
        private String podIP;
        // private List<PodIP> podIPs = new ArrayList();
        private String qosClass;
        private String reason;
        private String resize;
        //  private List<PodResourceClaimStatus> resourceClaimStatuses = new ArrayList();
        @JsonFormat(timezone = "UTC", pattern = Global.ISO8601)
        private Date startTime;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class PodCondition implements Serializable {
        @Serial
        private static final long serialVersionUID = -2976180260319668044L;
        @JsonFormat(timezone = "UTC", pattern = Global.ISO8601)
        private Date lastProbeTime;
        @JsonFormat(timezone = "UTC", pattern = Global.ISO8601)
        private Date lastTransitionTime;
        private String message;
        private String reason;
        private String status;
        private String type;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class PodSpec implements Serializable {
        @Serial
        private static final long serialVersionUID = 6481506875613911426L;
        private String nodeName;
    }

}
