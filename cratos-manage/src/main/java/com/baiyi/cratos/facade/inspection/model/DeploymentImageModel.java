package com.baiyi.cratos.facade.inspection.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/8/26 18:05
 * &#064;Version 1.0
 */
public class DeploymentImageModel {

    @Data
    @Builder
    public static class DeploymentImage {
        private String appName;
        private String deploymentName;
        private String image;
        private String env;
    }

    @Data
    @Builder
    public static class DeploymentImageInspection {
        private List<DeploymentImage> deploymentImages;
        private String appName;

        public boolean isConsistency() {
            if (CollectionUtils.isEmpty(deploymentImages)) {
                return true;
            }
            Set<String> imageNames = deploymentImages.stream()
                    .map(e -> e.image)
                    .collect(Collectors.toSet());
            return imageNames.size() == 1;
        }
    }

}
