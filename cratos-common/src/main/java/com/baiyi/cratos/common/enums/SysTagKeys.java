package com.baiyi.cratos.common.enums;

import lombok.Getter;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/19 11:35
 * &#064;Version 1.0
 */
@Getter
public enum SysTagKeys {

    COMMAND_EXEC_APPROVER("CommandExecApprover"),
    COMMAND_EXEC("CommandExec");

    private final String key;

    SysTagKeys(String key) {
        this.key = key;
    }

}
