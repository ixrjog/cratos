package com.baiyi.cratos.domain.param.rbac;

import com.baiyi.cratos.domain.generator.RbacRole;
import com.baiyi.cratos.domain.param.IToTarget;
import com.baiyi.cratos.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/1/17 17:17
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class RbacRoleParam {

    @Data
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class RolePageQuery extends PageParam {
        private String roleName;
    }

    @Data
    @Schema
    public static class UpdateRole implements IToTarget<RbacRole> {
        @NotNull(message = "The ID must be specified.")
        private Integer id;
        private String roleName;
        private Integer accessLevel;
        private Boolean workOrderVisible;
        private String comment;
    }

    @Data
    @Schema
    public static class AddRole implements IToTarget<RbacRole> {
        @NotNull
        private String roleName;
        @NotNull
        private Integer accessLevel;
        @NotNull
        private Boolean workOrderVisible;
        private String comment;
    }

}
