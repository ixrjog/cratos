package com.baiyi.cratos.domain.view.application;

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
 * &#064;Date  2024/11/20 11:31
 * &#064;Version 1.0
 */
public class ApplicationResourceKubernetesVO {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class KubernetesDetails implements Serializable {
        @Serial
        private static final long serialVersionUID = 6024185257685366861L;
        private ApplicationVO.Application application;
        private String namespace;
        private List<DeploymentVO> deployments;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class DeploymentVO implements Serializable {
        @Serial
        private static final long serialVersionUID = 9137044441466358774L;
        private List<PodVO> pods;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class PodVO implements Serializable {
        @Serial
        private static final long serialVersionUID = -438695425945757038L;
        private List<ContainerVO> containers;

        private String name;

        private String podIP;
        // private String node;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class ContainerVO implements Serializable {
        @Serial
        private static final long serialVersionUID = -7978494527647095842L;
        private String image;
    }


}
