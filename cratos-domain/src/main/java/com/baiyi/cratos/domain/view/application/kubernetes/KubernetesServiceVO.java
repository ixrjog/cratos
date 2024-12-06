package com.baiyi.cratos.domain.view.application.kubernetes;

import com.baiyi.cratos.domain.view.application.kubernetes.common.KubernetesCommonVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/3 10:57
 * &#064;Version 1.0
 */
public class KubernetesServiceVO {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class Service implements Serializable {
        @Serial
        private static final long serialVersionUID = -45376273071437967L;
        private KubernetesCommonVO.Metadata metadata;
        private ServiceSpec spec;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class ServiceSpec implements Serializable {
        @Serial
        private static final long serialVersionUID = 8106057816263739335L;
        private Map<String, String> selector;
        private List<ServicePort> ports;
        private String clusterIP;
        private String type;

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class ServicePort implements Serializable {
        @Serial
        private static final long serialVersionUID = -4735783248483509055L;
        private String appProtocol;
        private String name;
        private Integer nodePort;
        private Integer port;
        private String protocol;
        private Integer targetPort;
    }

}
