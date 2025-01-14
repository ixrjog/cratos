package com.baiyi.cratos.domain.view.application.kubernetes;

import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.view.access.AccessControlVO;
import com.baiyi.cratos.domain.view.application.ApplicationVO;
import com.google.common.collect.Lists;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/20 11:31
 * &#064;Version 1.0
 */
public class KubernetesVO {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    @BusinessType(type = BusinessTypeEnum.APPLICATION)
    public static class KubernetesDetails implements AccessControlVO.HasAccessControl, Serializable {
        @Serial
        private static final long serialVersionUID = 6024185257685366861L;

        public static KubernetesDetails failed(String message) {
            return KubernetesDetails.builder()
                    .message(message)
                    .success(false)
                    .build();
        }

        @Builder.Default
        private boolean success = true;
        private String message;
        private ApplicationVO.Application application;
        private Banner banner;
        private String namespace;
        private Workloads workloads;
        private Network network;

        @Schema(description = "访问控制")
        private AccessControlVO.AccessControl accessControl;

        @Override
        public Integer getBusinessId() {
            return Optional.ofNullable(this.application).map(ApplicationVO.Application::getId).orElse(0);
        }
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class Workloads implements Serializable {
        @Serial
        private static final long serialVersionUID = 6826239482231743285L;
        @Builder.Default
        private List<KubernetesDeploymentVO.Deployment> deployments = Lists.newArrayList();
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class Network implements Serializable {
        @Serial
        private static final long serialVersionUID = 6826239482231743285L;
        @Builder.Default
        private List<KubernetesServiceVO.Service> services = Lists.newArrayList();
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class Banner implements Serializable {
        @Serial
        private static final long serialVersionUID = 7690223104294560669L;
        private String name;
    }

}
