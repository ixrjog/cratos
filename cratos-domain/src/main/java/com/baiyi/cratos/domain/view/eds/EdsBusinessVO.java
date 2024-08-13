package com.baiyi.cratos.domain.view.eds;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/8/12 下午3:44
 * &#064;Version 1.0
 */
public class EdsBusinessVO {

    @Data
    @ToString
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class KubernetesInstanceResource implements Serializable {
        @Serial
        private static final long serialVersionUID = -1845071866024804853L;

        public static final KubernetesInstanceResource NO_RESOURCE = KubernetesInstanceResource.builder()
                .hasResource(false)
                .build();

        @Builder.Default
        private boolean hasResource = true;
        private Integer instanceId;
        private String instanceName;
        private String env;
        private String appName;
        @Schema(description = "key=资产类型枚举")
        private Map<String, List<EdsAssetVO.Asset>> resource;
    }

}
