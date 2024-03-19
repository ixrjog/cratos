package com.baiyi.cratos.domain.param.rbac;

import com.baiyi.cratos.domain.generator.RbacUserRole;
import com.baiyi.cratos.domain.param.IToTarget;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2024/3/18 16:03
 * @Version 1.0
 */
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

}
