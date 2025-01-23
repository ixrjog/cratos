package com.baiyi.cratos.domain.param.http.user;

import com.baiyi.cratos.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/1/17 10:17
 * &#064;Version 1.0
 */
public class UserPermissionBusinessParam {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class UserPermissionBusinessPageQuery extends PageParam {
        @Schema(description = "查询名称")
        private String queryName;
        private String username;
        private String businessType;
    }

    /**
     * 批量授权
     */
    @Data
    @Schema
    @Builder
    public static class UpdateUserPermissionBusiness {
        @NotBlank
        private String username;
        private String name;
        @NotBlank
        private String businessType;
        @NotEmpty
        private List<BusinessPermission> businessPermissions;
    }

    @Data
    @Schema
    @Builder
    public static class BusinessPermission {
        private Integer businessId;
        private List<RoleMember> roleMembers;
    }

    @Data
    @Schema
    @Builder
    public static class RoleMember {
        private String role;
        private Boolean checked;
        private Date expiredTime;
    }

}
