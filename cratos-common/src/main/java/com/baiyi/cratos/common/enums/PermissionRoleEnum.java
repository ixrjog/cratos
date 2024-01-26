package com.baiyi.cratos.common.enums;

import lombok.Getter;

/**
 * @Author baiyi
 * @Date 2024/1/19 10:14
 * @Version 1.0
 */
@Getter
public enum PermissionRoleEnum {

    /**
     * 用户授权的业务对象角色
     */
    BASE(0),
    OWNER(10);

    private final int code;

    PermissionRoleEnum(int code) {
        this.code = code;
    }

}
