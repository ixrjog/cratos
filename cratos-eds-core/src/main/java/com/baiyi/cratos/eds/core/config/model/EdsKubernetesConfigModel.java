package com.baiyi.cratos.eds.core.config.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/3/7 10:06
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class EdsKubernetesConfigModel {

    @Data
    @NoArgsConstructor
    @Schema
    public static class AmazonEks {
        private String region;
        private String clusterName;
        private String url;
        private Cred cred;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Cred {
        private String accessKey;
        private String secretKey;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Kubeconfig {
        private String path;
        @Schema(description = "Kubeconfig context name")
        private String useContext;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Filter {
        private Namespace namespace;
        private Container container;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Namespace {
        private List<String> exclude;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Container {
        private List<String> exclude;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class KubernetesGrafana {
        private GrafanaKubernetes kubernetes;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class GrafanaKubernetes {
        private String overview;
        private String workload;
        private GrafanaKubernetesPod pod;
        private GrafanaKubernetesNode node;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class GrafanaKubernetesPod {
        private String overview;
        private String topN;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class GrafanaKubernetesNode {
        private String overview;
        private String topN;
        private String summary;
        private String pool;
    }

}
