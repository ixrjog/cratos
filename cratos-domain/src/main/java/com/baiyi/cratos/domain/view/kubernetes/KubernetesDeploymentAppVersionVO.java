package com.baiyi.cratos.domain.view.kubernetes;

import com.baiyi.cratos.domain.generator.EdsAsset;
import com.baiyi.cratos.domain.view.CachedVO;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/6/3 17:17
 * &#064;Version 1.0
 */
public class KubernetesDeploymentAppVersionVO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeploymentAssets {
        @Builder.Default
        private List<EdsAsset> dcAssets = Lists.newArrayList();
        @Builder.Default
        private List<EdsAsset> drAssets = Lists.newArrayList();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ComparisonVersion implements CachedVO.ICached, Serializable {
        @Serial
        private static final long serialVersionUID = 7489528592358298409L;
        private List<ApplicationVersion> applicationVersions;
        @Builder.Default
        private CachedVO.Cached cached = CachedVO.Cached.builder()
                .build();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApplicationVersion implements Serializable {
        @Serial
        private static final long serialVersionUID = 2870753709768723901L;
        // 应用名称
        private String appName;
        private List<DeploymentImage> dcDeploymentImages;
        private List<DeploymentImage> drDeploymentImages;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeploymentImage implements Serializable {
        @Serial
        private static final long serialVersionUID = -7632770100903369707L;
        // 应用名称
        private String name;
        private String image;
    }

}
