package com.baiyi.cratos.domain.param.http.user;

import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.generator.UserPermission;
import com.baiyi.cratos.domain.param.IToTarget;
import com.baiyi.cratos.domain.param.PageParam;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/20 10:00
 * &#064;Version 1.0
 */
public class UserPermissionParam {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class UserPermissionPageQuery extends PageParam {
        @Schema(description = "查询名称")
        private String queryName;
        private String username;
        private String businessType;
    }

    @Data
    @Schema
    @Builder
    public static class GrantUserPermission implements IToTarget<UserPermission> {
        @NotBlank
        private String username;
        private String name;
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

    @Data
    @Schema
    @Builder
    public static class QueryBusinessUserPermissionDetails implements BaseBusiness.HasBusiness {
        @NotBlank
        private String businessType;
        @NotNull
        private Integer businessId;
        private String username;
    }

    @Data
    @Schema
    @Builder
    public static class QueryAllBusinessUserPermissionDetails {
        @NotBlank
        private String businessType;
        private String username;
    }

}
