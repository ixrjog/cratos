package com.baiyi.cratos.domain.param.http.rbac;

import com.baiyi.cratos.domain.generator.RbacUserRole;
import com.baiyi.cratos.domain.param.IToTarget;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/3/18 16:03
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class RbacUserRoleParam {

    @Data
    @Schema
    public static class AddUserRole implements IToTarget<RbacUserRole> {
        @NotNull(message = "Username must be specified.")
        private String username;
        @NotNull(message = "RoleId must be specified.")
        private Integer roleId;
    }

    @Data
    @Schema
    public static class DeleteUserRole implements IToTarget<RbacUserRole> {
        @NotNull(message = "Username must be specified.")
        private String username;
        @NotNull(message = "RoleId must be specified.")
        private Integer roleId;
    }

    @Data
    @Schema
    public static class VerifyUserRoleResourcePermission {
        @NotNull(message = "Username must be specified.")
        private String username;
        @NotNull(message = "ResourceId must be specified.")
        private Integer resourceId;
    }

}
