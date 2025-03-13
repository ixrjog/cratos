package com.baiyi.cratos.eds.opscloud.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Tag {
        private String tagKey;
    }

}
