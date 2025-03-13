package com.baiyi.cratos.eds.opscloud.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/12 13:38
 * &#064;Version 1.0
 */
public class OcUserVO {

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class User {
        private Integer id;
        @Schema(description = "头像")
        private String avatar;
        private Map<String, List<UserPermissionVO.UserPermission>> businessPermissions;
        private String username;
        private String uuid;
        private String displayName;
        private String name;
        private String email;
        private Boolean isActive = true;
        private String wechat;
        @Schema(description = "手机")
        private String phone;
        private Boolean mfa;
        private Boolean forceMfa;
        private String createdBy;
        private String source;
        private String comment;

    }

}
