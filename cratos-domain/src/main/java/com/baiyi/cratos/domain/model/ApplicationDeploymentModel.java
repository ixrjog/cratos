package com.baiyi.cratos.domain.model;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

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

}
