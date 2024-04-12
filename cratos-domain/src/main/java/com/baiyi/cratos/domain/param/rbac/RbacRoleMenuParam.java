package com.baiyi.cratos.domain.param.rbac;

import com.baiyi.cratos.domain.generator.RbacRoleMenu;
import com.baiyi.cratos.domain.param.IToTarget;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/4/8 下午3:13
 * @Version 1.0
 */
public class RbacRoleMenuParam {

    public interface IToRoleMenus {

        List<Integer> getMenuIds();

        Integer getRoleId();

        default List<RbacRoleMenu> toRoleMenus() {
            return getMenuIds().stream()
                    .map(e -> RbacRoleMenu.builder()
                            .menuId(e)
                            .roleId(getRoleId())
                            .build())
                    .toList();
        }

    }

    @Data
    @Schema
    public static class AddRoleMenu implements IToTarget<RbacRoleMenu> {

        private Integer roleId;

        private Integer menuId;

    }

    @Data
    @Schema
    public static class SaveRoleMenu implements IToRoleMenus {

        @NotNull
        private Integer roleId;

        @NotEmpty
        private List<Integer> menuIds;

    }

}
