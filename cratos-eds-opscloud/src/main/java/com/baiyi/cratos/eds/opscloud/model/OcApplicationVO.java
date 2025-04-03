package com.baiyi.cratos.eds.opscloud.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/2 11:18
 * &#064;Version 1.0
 */
public class OcApplicationVO {

    @Data
    @NoArgsConstructor
    @Schema
    public static class Application {
        @Schema(description = "主键", example = "1")
        private Integer id;
        @Schema(description = "应用名称")
        private String name;
        @Schema(description = "应用关键字")
        private String applicationKey;
        private Integer applicationType;
        @Schema(description = "描述")
        private Boolean isActive;
        private String comment;
        private List<Tag> tags;
        private UserPermissionVO.UserPermission userPermission;
        private Map<String, List<Resource>> resourceMap;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Tag {
        private String tagKey;
    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class Resource {
        private String instanceUuid;
        @Schema(description = "主键", example = "1")
        private Integer id;
        private Integer applicationId;
        private String name;
        @Schema(description = "虚拟资源", example = "true")
        @Builder.Default
        private Boolean virtualResource = false;
        private String resourceType;
        private Integer businessId;
        private Integer businessType;
        private String comment;
        /**
         * 前端选择用
         */
        @Builder.Default
        private Boolean checked = false;
    }

}
