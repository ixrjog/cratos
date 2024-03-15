package com.baiyi.cratos.domain.view.rbac;

import com.baiyi.cratos.domain.view.BaseVO;
import com.baiyi.cratos.domain.view.IResourceCount;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2024/1/17 17:20
 * @Version 1.0
 */
public class RbacRoleVO {

    public interface IRbacRole {
        Integer getRbacRoleId();
        void setRbacRole(Role rbacRole);
    }

    public interface IRbacRoles {
        String getUsername();
        void setRbacRoles(List<Role> rbacRoles);
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Schema
    public static class Role extends BaseVO implements IResourceCount {

        private Integer id;

        private String roleName;

        private Integer accessLevel;

        private Boolean workOrderVisible;

        private String comment;

        @Schema(description = "Resource Count")
        private Map<String, Integer> resourceCount;

    }

}
