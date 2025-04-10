package com.baiyi.cratos.domain.param.http.rbac;

import com.baiyi.cratos.domain.generator.RbacRoleMenu;
import com.baiyi.cratos.domain.param.IToTarget;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2024/4/8 下午3:13
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
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
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema
    public static class SaveRoleMenu implements IToRoleMenus {
        @NotNull
        private Integer roleId;
        @NotNull
        private List<Integer> menuIds;
    }

}
