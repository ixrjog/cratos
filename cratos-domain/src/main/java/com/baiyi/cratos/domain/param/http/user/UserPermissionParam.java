package com.baiyi.cratos.domain.param.http.user;

import com.baiyi.cratos.domain.BaseBusiness;
import com.baiyi.cratos.domain.generator.UserPermission;
import com.baiyi.cratos.domain.param.IToTarget;
import com.baiyi.cratos.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/20 10:00
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
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
        @NotBlank
        private String businessType;
        private Integer businessId;
        @NotBlank
        private String role;
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

    @Data
    @Schema
    @Builder
    public static class QueryUserPermissionByBusiness implements BaseBusiness.HasBusiness {
        @NotBlank
        private String businessType;
        @NotNull
        private Integer businessId;
    }

}
