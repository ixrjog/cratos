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
    public static class Deployment implements Serializable {
        @Serial
        private static final long serialVersionUID = 9137044441466358774L;
        private KubernetesCommonVO.Metadata metadata;
        private DeploymentSpec spec;
        private List<KubernetesPodVO.Pod> pods;
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
    }

}
