package com.baiyi.cratos.domain.view.application.kubernetes;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/21 16:13
 * &#064;Version 1.0
 */
public class KubernetesCommonVO {

    public interface HasKubernetesCluster {
        void setKubernetesCluster(KubernetesCluster kubernetesCluster);
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class Metadata implements Serializable {
        @Serial
        private static final long serialVersionUID = -8726652568141005882L;
        private String namespace;
        private String name;
        private String generateName;
        private String uid;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class KubernetesCluster implements Serializable {
        @Serial
        private static final long serialVersionUID = 5892072872711777553L;
        private String name;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class TopologyDetails implements Serializable {
        @Serial
        private static final long serialVersionUID = -2123854130035790444L;
        // private Map<{zone}, Map<{nodeName}, KubernetesNode>>
        private Map<String, Map<String, KubernetesNode>> nodeTopology;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class KubernetesNode implements Serializable {
        @Serial
        private static final long serialVersionUID = 2768461856912014422L;
        private String name;
        private String hostIP;
        private String region;
        private String zone;
    }

}
