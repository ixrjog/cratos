package com.baiyi.cratos.eds.opscloud.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/12 13:56
 * &#064;Version 1.0
 */
public class OcUserParam {

    @SuperBuilder(toBuilder = true)
    @Data
    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class UserPageQuery extends HasPageParam {
        @Schema(description = "模糊查询")
        private String queryName;
        @Schema(description = "过滤系统标签对象")
        private Boolean filterTag;
        @Schema(description = "展开")
        private Boolean extend;
        @Schema(description = "有效")
        private Boolean isActive;
        @Schema(description = "标签ID")
        private Integer tagId;
    }

    @Data
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class UserBusinessPermissionPageQuery extends HasPageParam {

        private int businessType;

        @Schema(description = "查询名称")
        private String queryName;

        @Schema(description = "应用ID")
        private Integer applicationId;

        @Schema(description = "用户ID")
        private Integer userId;

        @Schema(description = "是否授权")
        @NotNull(message = "是否授权选项不能为空")
        @Builder.Default
        private Boolean authorized = true;

        @Schema(description = "是否管理员")
        @Builder.Default
        private Boolean admin = false;

        @Builder.Default
        private Boolean extend = false;

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class AddUser {
        private String username;
        private String displayName;
        private String comment;
        private Integer id;
        @Builder.Default
        private Boolean isActive = true;
        private String name;
        private Boolean needInitializeDefaultConfiguration = true;
        private String password;
        private String phone;
        private String email;
        private String wechat;
    }

}
