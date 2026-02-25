package com.baiyi.cratos.domain.view.rbac;

import com.baiyi.cratos.domain.view.BaseVO;
import com.baiyi.cratos.domain.view.HasResourceCount;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/1/17 17:20
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class RbacRoleVO {

    public interface HasRbacRole {
        Integer getRbacRoleId();

        void setRbacRole(Role rbacRole);
    }

    public interface HasRbacRoles {
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

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class RoleDetails implements Serializable {
        @Serial
        private static final long serialVersionUID = -6399555257323642205L;
        private Role role;
        private List<GroupResource> groupResources;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class GroupResource implements Serializable {
        @Serial
        private static final long serialVersionUID = 5918841651409551250L;
        private RbacGroupVO.Group group;
        private List<RbacResourceVO.Resource> resources;
    }

}
