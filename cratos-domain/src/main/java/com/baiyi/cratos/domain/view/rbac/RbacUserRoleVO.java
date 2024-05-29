package com.baiyi.cratos.domain.view.rbac;

import com.baiyi.cratos.domain.view.BaseVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @Author baiyi
 * @Date 2024/1/18 09:54
 * @Version 1.0
 */
public class RbacUserRoleVO {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Schema
    public static class UserRole extends BaseVO implements RbacRoleVO.HasRbacRole {

        @Serial
        private static final long serialVersionUID = 7828537753029882531L;

        private Integer id;

        private String username;

        private Integer roleId;

        private RbacRoleVO.Role rbacRole;

        @Override
        public Integer getRbacRoleId() {
            return roleId;
        }

    }

}
