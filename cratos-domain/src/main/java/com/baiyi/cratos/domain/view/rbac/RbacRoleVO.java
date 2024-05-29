package com.baiyi.cratos.domain.view.rbac;

import com.baiyi.cratos.domain.view.BaseVO;
import com.baiyi.cratos.domain.view.HasResourceCount;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2024/1/17 17:20
 * @Version 1.0
 */
public class RbacRoleVO {

    public interface HasRbacRole {
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
    public static class Role extends BaseVO implements HasResourceCount {

        @Serial
        private static final long serialVersionUID = -537492408171499126L;

        private Integer id;

        private String roleName;

        private Integer accessLevel;

        private Boolean workOrderVisible;

        private String comment;

        @Schema(description = "Resource Count")
        private Map<String, Integer> resourceCount;

    }

}
