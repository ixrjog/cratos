package com.baiyi.cratos.domain.view.application.kubernetes;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/21 11:34
 * &#064;Version 1.0
 */
public class KubernetesContainerVO {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(type = "pod.containerStatuses")
    public static class ContainerStatus implements Serializable {
        @Serial
        private static final long serialVersionUID = -7978494527647095842L;
        @Schema(description = "Main Container")
        private Boolean main;
        private String name;
        private String containerID;
        private String image;
        private String imageID;
        private Boolean started;
        private Integer restartCount;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ImageVersion implements Serializable {
        @Serial
        private static final long serialVersionUID = -7907030787557723546L;

        public static ImageVersion notFound(String image) {
            return ImageVersion.builder()
                    .image(image)
                    .isExist(false)
                    .build();
        }

        @Builder.Default
        private boolean isExist = true;
        private String image;
        private String versionName;
        private String versionDesc;
    }

}
