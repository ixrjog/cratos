package com.baiyi.cratos.eds.core.config;

import com.baiyi.cratos.eds.core.config.base.IEdsConfigModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/3/7 10:06
 * @Version 1.0
 */
public class EdsKubernetesConfigModel {

    @Data
    @NoArgsConstructor
    @Schema
    public static class Kubernetes implements IEdsConfigModel {

        private String version;

        private String provider;

        @Schema(description = "Amazon EKS cred")
        private AmazonEks amazonEks;

        private Kubeconfig kubeconfig;

        private Filter filter;

    }

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

}
