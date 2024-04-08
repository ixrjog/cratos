package com.baiyi.cratos.domain.param.rbac;

import com.baiyi.cratos.domain.generator.RbacRoleMenu;
import com.baiyi.cratos.domain.param.IToTarget;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2024/4/8 下午3:13
 * @Version 1.0
 */
public class RbacRoleMenuParam {

    @Data
    @Schema
    public static class AddRoleMenu implements IToTarget<RbacRoleMenu> {

        private Integer roleId;

        private Integer menuId;

    }

}
