package com.baiyi.cratos.domain.model;

import com.google.common.collect.Lists;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/6/16 16:32
 * &#064;Version 1.0
 */
public class AppFinOpsModel {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class AppCost implements Serializable {
        @Serial
        private static final long serialVersionUID = -5098983173685093597L;
        private String applicationName;
        @Builder.Default
        private List<AppResource> appResources = Lists.newArrayList();
        private Integer totalReplicas;

        public void addAppResource(AppResource appResource) {
            if (appResource == null) {
                return;
            }
            if (appResources == null) {
                appResources = Lists.newArrayList();
            }
            appResources.add(appResource);
            this.totalReplicas = acqTotalReplicas();
        }

        public Integer acqTotalReplicas() {
            if (CollectionUtils.isEmpty(appResources)) {
                return 0;
            }
            return appResources.stream()
                    .map(AppResource::getReplicas)
                    .filter(Objects::nonNull)
                    .reduce(0, Integer::sum);
        }

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class AppResource implements Serializable {
        @Serial
        private static final long serialVersionUID = -9153675711213891193L;
        private String resourceName;
        private String resourceType;
        private Integer replicas;
    }

}
