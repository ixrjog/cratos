package com.baiyi.cratos.domain.param.http.user;

import com.baiyi.cratos.domain.YamlDump;
import com.baiyi.cratos.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
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
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    @Builder
    public static class UpdateUserPermissionBusiness {
        @NotBlank
        private String username;
        @NotBlank
        private String businessType;
        @NotEmpty
        private List<BusinessPermission> businessPermissions;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    @Builder
    public static class BusinessPermission extends YamlDump {
        private Integer businessId;
        private String name;
        private List<RoleMember> roleMembers;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    @Builder
    public static class RoleMember {
        private String role;
        private Boolean checked;
        private Date expiredTime;
    }

}
