package com.baiyi.cratos.domain.param.http.rbac;

import com.baiyi.cratos.domain.generator.RbacRoleResource;
import com.baiyi.cratos.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/3/14 17:24
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class RbacRoleResourceParam {

    public interface IToRbacRoleResources {
        Integer getRoleId();
        List<Integer> getResourceIds();
        default List<RbacRoleResource> toRbacRoleResources() {
            return getResourceIds().stream()
                    .map(resourceId -> RbacRoleResource.builder()
                            .resourceId(resourceId)
                            .roleId(getRoleId())
                            .build())
                    .toList();
        }
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @Schema
    public static class RoleResourcePageQuery extends PageParam {
        @Schema(description = "资源组ID")
        private Integer groupId;
        @Schema(description = "角色ID")
        private Integer roleId;
        @Schema(description = "是否在角色中")
        private Boolean inRole;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class AddRoleResource implements IToRbacRoleResources {
        @Schema(description = "角色ID")
        @NotNull
        private Integer roleId;
        @Schema(description = "资源ID")
        @NotEmpty
        private List<Integer> resourceIds;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class CopyRoleResource {
        @Schema(description = "角色ID")
        @NotNull
        private Integer roleId;
        @Schema(description = "角色ID")
        @NotNull
        private Integer targetRoleId;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class DeleteRoleResource implements IToRbacRoleResources {
        @Schema(description = "角色ID")
        @NotNull
        private Integer roleId;
        @Schema(description = "资源ID")
        @NotEmpty
        private List<Integer> resourceIds;
    }

}
