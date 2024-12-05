package com.baiyi.cratos.domain.param.http.user;

import com.baiyi.cratos.domain.generator.UserPermission;
import com.baiyi.cratos.domain.param.IToTarget;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/20 10:00
 * &#064;Version 1.0
 */
public class UserPermissionParam {

    @Data
    @Schema
    @Builder
    public static class GrantUserPermission implements IToTarget<UserPermission> {
        @NotBlank
        private String username;
        private String name;
        @NotBlank
        private String displayName;
        @NotBlank
        private String businessType;
        private Integer businessId;
        @NotBlank
        private String role;
        private Boolean valid;
        private Integer seq;
        private String content;
        @JsonFormat(timezone = "UTC", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date expiredTime;
        private String comment;
    }

    @Data
    @Schema
    @Builder
    public static class RevokeUserPermission implements IToTarget<UserPermission> {
        @NotBlank
        private String username;
        @NotBlank
        private String businessType;
        @NotNull
        private Integer businessId;
        @NotBlank
        private String role;
    }

}
