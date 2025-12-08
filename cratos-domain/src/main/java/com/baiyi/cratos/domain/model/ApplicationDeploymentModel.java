package com.baiyi.cratos.domain.model;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.view.application.ApplicationVO;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.domain.view.eds.EdsInstanceVO;
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
 * &#064;Date  2025/4/30 11:40
 * &#064;Version 1.0
 */
public class ApplicationDeploymentModel {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class DeploymentScale implements Serializable {
        @Serial
        private static final long serialVersionUID = 3183391109623141815L;
        private EdsAssetVO.Asset deployment;
        private String namespace;
        private Integer currentReplicas;
        private Integer expectedReplicas;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class DeleteDeploymentPod implements Serializable {
        @Serial
        private static final long serialVersionUID = 3183391109623141815L;
        private String namespace;
        @Schema(description = "Kubernetes deployment asset")
        private EdsAsset asset;
        private String podName;
        private Integer ticketId;
        private String ticketNo;
        @Builder.Default
        private Boolean success = true;
        private String result;
        @Builder.Default
        private Date deleteOperationTime = new Date();
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class RedeployDeployment implements Serializable {
        @Serial
        private static final long serialVersionUID = -1509979803332251572L;
        private String namespace;
        @Schema(description = "Kubernetes deployment asset")
        private EdsAsset asset;
        private Integer ticketId;
        private String ticketNo;
        @Builder.Default
        private Boolean success = true;
        private String result;
        @Builder.Default
        private Date redeployOperationTime = new Date();
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class DeploymentJvmSpec implements Serializable {
        // 前端需要传参 applicationName assetId jvmSpecType
        @Serial
        private static final long serialVersionUID = -1789987906801176169L;
        private String applicationName;
        // deployment asset id
        private Integer assetId;
        @Schema(description = "Kubernetes deployment asset")
        private EdsAssetVO.Asset deployment;
        private String jvmSpecType;
        private ApplicationVO.Application application;
        private EdsInstanceVO.EdsInstance edsInstance;
        private String namespace;
        // 资源需求
        private ResourceRequirements resourceRequirements;
        private String modifiedJavaOpts;
        // 多行参数，用于前端展示
        private List<String> JavaOptsArgs;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class ResourceRequirements implements Serializable {
        @Serial
        private static final long serialVersionUID = -1509979803332251572L;
        private Map<String, String> limits;
        private Map<String, String> requests;
    }

}
